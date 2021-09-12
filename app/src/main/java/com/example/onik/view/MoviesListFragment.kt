package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.Foo.Companion.movies
import com.example.onik.R
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MoviesCollectionViewModel
import com.example.onik.viewmodel.Constants
import com.google.android.material.snackbar.Snackbar


class MoviesListFragment : Fragment(), Constants {

    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MoviesListFragment {
            val fragment = MoviesListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var movieListType: String = ""

    private lateinit var viewModel: MoviesCollectionViewModel
    private var _binding: MoviesListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MoviesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MoviesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        movieListType = arguments?.getString(MovieFragment.BUNDLE_EXTRA)!!

        viewModel = ViewModelProvider(this).get(MoviesCollectionViewModel::class.java)
        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getMoviesListLiveData(movieListType)?.observe(viewLifecycleOwner, observer)
        viewModel.getDataFromRemoteSource(movieListType)
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.LoadingMovies -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovies -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.moviesData = appState.movies
            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.container, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getDataFromRemoteSource(movieListType) }
                    .show()
            }
            else -> {
            }
        }
    }


    private fun initRecyclerView() {
        adapter = MoviesAdapter(R.layout.item)
        adapter.listener = MoviesAdapter.OnItemViewClickListener { idMovie ->
            val bundle = Bundle()
            bundle.putInt(MovieFragment.BUNDLE_EXTRA, idMovie)

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MovieFragment.newInstance(bundle))
                .addToBackStack(null)
                .commit()
        }

        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.mainRecyclerView.setHasFixedSize(true);
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}