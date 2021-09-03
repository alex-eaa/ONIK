package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.Foo.Companion.movies
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

    private lateinit var viewModel: ViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MoviesAdapter
    private lateinit var mainRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        initRecyclerView()
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
                adapter.setData(appState.movies)
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


    private fun initRecyclerView() {
        mainRecyclerView = binding.mainRecyclerView
        adapter = MoviesAdapter { position -> onListItemClick(position) }
        mainRecyclerView.adapter = adapter
        mainRecyclerView.layoutManager = GridLayoutManager(context, 2)
        mainRecyclerView.setHasFixedSize(true);
    }


    private fun onListItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(MovieFragment.BUNDLE_EXTRA, movies[position].id)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, MovieFragment.newInstance(bundle))
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}