package com.example.onik.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

class MovieFragment : Fragment() {

    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MovieFragment {
            val fragment = MovieFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var idMovie: Int = 0

    private lateinit var viewModel: MovieViewModel
    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idMovie = arguments?.getInt(BUNDLE_EXTRA)!!

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        //В две строки
//        val observer = Observer<AppState> { appState -> renderData(appState) }
//        viewModel.getMovieDetailsLiveData().observe(viewLifecycleOwner, observer)

        //Можно записать в одну строку
        viewModel.movieDetailsLiveData
            .observe(viewLifecycleOwner, { appState -> renderData(appState) })

        viewModel.getDataFromRemoteSource(idMovie)
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovie -> {
                binding.loadingLayout.visibility = View.GONE
                binding.title.text = appState.movie.title
                binding.voteAverage.text =
                    "${appState.movie.vote_average} (${appState.movie.vote_count})"
                binding.overview.text = appState.movie.overview
                binding.runtime.text = "${appState.movie.runtime} ${getString(R.string.min)}"
                binding.releaseDate.text = appState.movie.release_date
                binding.budget.text = appState.movie.budget.toString()
                binding.revenue.text = appState.movie.revenue.toString()

                var genres = ""
                appState.movie.genre_ids.forEach { genres += "${it.name}, " }
                binding.genre.text = genres.dropLast(2)

            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.main, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getDataFromRemoteSource(idMovie) }
                    .show()
            }
            else -> {
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}