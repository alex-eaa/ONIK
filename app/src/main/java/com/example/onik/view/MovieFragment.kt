package com.example.onik.view

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.model.MovieLoader
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MainBroadcastReceiver
import com.example.onik.viewmodel.MovieViewModel

class MovieFragment : Fragment() {
    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MovieFragment =
            MovieFragment().apply { arguments = bundle }
    }

    private var idMovie: Int = 0
    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt(BUNDLE_EXTRA)?.let { it ->
            idMovie = it
            viewModel.movieDetailsLiveData
                .observe(viewLifecycleOwner, { appState -> renderData(appState) })
            viewModel.getDataFromRemoteSource(idMovie)
        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovie -> {
                binding.apply {
                    loadingLayout.hide()
                    title.text = appState.movie?.title
                    voteAverage.text =
                        "${appState.movie?.vote_average} (${appState.movie?.vote_count})"
                    overview.text = appState.movie?.overview
                    runtime.text = "${appState.movie?.runtime} ${getString(R.string.min)}"
                    releaseDate.text = appState.movie?.release_date
                    budget.text = appState.movie?.budget.toString()
                    revenue.text = appState.movie?.revenue.toString()
                }

                var genres = ""
                appState.movie?.genres?.forEach { genres += "${it.name}, " }
                binding.genre.text = genres.dropLast(2)

            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(action = {
                    viewModel.getDataFromRemoteSource(idMovie)
                })
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}