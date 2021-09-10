package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.R
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MoviesCollectionViewModel
import com.example.onik.viewmodel.Constants
import com.google.android.material.snackbar.Snackbar


class MoviesListFragment : Fragment(), Constants {

    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MoviesListFragment =
            MoviesListFragment().apply { arguments = bundle }
    }

    private var moviesCollectionName: String = ""
    private var _binding: MoviesListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesCollectionViewModel by lazy {
        ViewModelProvider(this).get(MoviesCollectionViewModel::class.java)
    }

    private val myAdapter: MoviesAdapter by lazy { MoviesAdapter(R.layout.item) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MoviesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(MovieFragment.BUNDLE_EXTRA)?.let { it ->
            initRecyclerView()
            moviesCollectionName = it
            viewModel.getMoviesListLiveData(moviesCollectionName)
                ?.observe(viewLifecycleOwner, { appState -> renderData(appState) })
            viewModel.getDataFromRemoteSource(moviesCollectionName)
        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.LoadingMovies -> binding.loadingLayout.show()

            is AppState.SuccessMovies -> {
                binding.loadingLayout.hide()
                myAdapter.moviesData = appState.movies
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar("Коллекцию не удалось загрузить.",
                    "Повторить",
                    action = { viewModel.getDataFromRemoteSource(moviesCollectionName) })
            }
        }
    }


    private fun initRecyclerView() {
        myAdapter.listener = MoviesAdapter.OnItemViewClickListener { movie ->
            activity?.supportFragmentManager?.let { fragmentManager ->
                fragmentManager.beginTransaction()
                    .replace(R.id.container, MovieFragment.newInstance(Bundle().apply {
                        putInt(MovieFragment.BUNDLE_EXTRA, movie.id)
                    }))
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.mainRecyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true);
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}