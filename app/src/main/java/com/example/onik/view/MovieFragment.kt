package com.example.onik.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.model.*
import com.example.onik.viewmodel.AppState
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

    private val localResultBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(RESULT_EXTRA)) {
                SUCCESS_RESULT -> renderData(AppState.SuccessMovie(intent.getParcelableExtra(
                    DETAILS_EXTRA)))
                ERROR_RESULT -> renderData(AppState.ErrorMessage(intent.getStringExtra(ERROR_EXTRA)))
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подпысываемся на Broadcast
//        context?.registerReceiver(localResultBroadcastReceiver, IntentFilter(DETAILS_INTENT_FILTER))

        // Подпысываемся на локальный Broadcast
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(localResultBroadcastReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
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
//            viewModel.getDataFromRemoteSource(idMovie)
            viewModel.startServiceDetailsLoader(context, idMovie)
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

            is AppState.ErrorMessage -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(text = appState.message.toString(),
                    action = { viewModel.getDataFromRemoteSource(idMovie) }
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Отпысываемся от Broadcast
//        context?.unregisterReceiver(localResultBroadcastReceiver)

        // Отпысываемся от локального Broadcast
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(localResultBroadcastReceiver)
        }
    }

}