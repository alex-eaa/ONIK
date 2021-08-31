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
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        viewModel.getPopularMoviesFromRemoteSource()
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovies -> {
                binding.loadingLayout.visibility = View.GONE

                binding.title0.text = appState.movies[0].title
                binding.releaseDate0.text = appState.movies[0].release_date

                binding.title1.text = appState.movies[1].title
                binding.releaseDate1.text = appState.movies[1].release_date
            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.main, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getPopularMoviesFromRemoteSource() }
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