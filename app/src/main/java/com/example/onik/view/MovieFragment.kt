package com.example.onik.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.onik.R
import com.example.onik.databinding.MainFragmentBinding
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.model.Movie
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MainViewModel
import com.example.onik.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar

class MovieFragment : Fragment() {


    companion object {
        fun newInstance() = MovieFragment()
    }

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
        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getMovieFromRemoteSource()


    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovie -> {
                binding.loadingLayout.visibility = View.GONE
                binding.title.text = appState.movie.title
                binding.releaseDate.text = appState.movie.release_date
                binding.overview.text = appState.movie.overview
            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.main, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getMovieFromRemoteSource() }
                    .show()
            }
            else -> {}
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}