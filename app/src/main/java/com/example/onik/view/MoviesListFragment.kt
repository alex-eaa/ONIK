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
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MoviesListViewModel
import com.google.android.material.snackbar.Snackbar


class MoviesListFragment : Fragment() {

    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MoviesListFragment {
            val fragment = MoviesListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var idList: Int = 0

    private lateinit var viewModel: MoviesListViewModel
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

        idList = arguments?.getInt(MovieFragment.BUNDLE_EXTRA)!!

        viewModel = ViewModelProvider(this).get(MoviesListViewModel::class.java)
        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getMoviesListLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getDataFromRemoteSource(idList)
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovies -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.moviesData = appState.movies
            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.container, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getDataFromRemoteSource(idList) }
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