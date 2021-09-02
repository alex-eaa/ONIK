package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.databinding.MainFragmentBinding
import com.example.onik.model.Movie
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.ViewModel
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var movies: Array<Movie>

    private lateinit var viewModel: ViewModel

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
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getPopularMoviesLiveData().observe(viewLifecycleOwner, observer)

        viewModel.getPopularMoviesFromRemoteSource()
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovies -> {
                binding.loadingLayout.visibility = View.GONE
                movies = appState.movies
                initRecyclerView(binding.mainRecyclerView, appState.movies)
            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.main, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getPopularMoviesFromRemoteSource() }
                    .show()
            }
            else -> {
            }
        }
    }


    private fun initRecyclerView(mainRecyclerView: RecyclerView, movies: Array<Movie>) {
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.layoutManager = GridLayoutManager(context, 2)
        mainRecyclerView.adapter = MoviesAdapter(movies) { position -> onListItemClick(position) }
    }


    private fun onListItemClick(position: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, MovieFragment.newInstance(movies[position].id))
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}