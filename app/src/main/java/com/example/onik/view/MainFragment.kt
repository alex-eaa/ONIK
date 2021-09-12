package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.databinding.MainFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MoviesCollectionViewModel
import com.example.onik.viewmodel.Constants
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_1
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_2
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_3
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment(), View.OnClickListener, Constants {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MoviesCollectionViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapAdapters: Map<String, MoviesAdapter>
    private lateinit var mapRecyclerView: Map<String, RecyclerView>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding =
            MainFragmentBinding.bind(inflater.inflate(R.layout.main_fragment, container, false))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.categoryTitleLayout1.setOnClickListener(this)
        binding.categoryTitleLayout2.setOnClickListener(this)
        binding.categoryTitleLayout3.setOnClickListener(this)

        viewModel = ViewModelProvider(this).get(MoviesCollectionViewModel::class.java)
        val observer = Observer<AppState> { appState -> renderData(appState) }

        viewModel.getMoviesListLiveData(MOVIES_COLLECTION_1)?.observe(viewLifecycleOwner, observer)
        viewModel.getMoviesListLiveData(MOVIES_COLLECTION_2)?.observe(viewLifecycleOwner, observer)
        viewModel.getMoviesListLiveData(MOVIES_COLLECTION_3)?.observe(viewLifecycleOwner, observer)

        viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_1)
        viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_2)
        viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_3)
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.LoadingMovies -> {
                when (appState.key) {
                    MOVIES_COLLECTION_1 -> binding.loadingLayout1.visibility = View.VISIBLE
                    MOVIES_COLLECTION_2 -> binding.loadingLayout2.visibility = View.VISIBLE
                    MOVIES_COLLECTION_3 -> binding.loadingLayout3.visibility = View.VISIBLE
                }
            }

            is AppState.SuccessMovies -> {
                when (appState.key) {
                    MOVIES_COLLECTION_1 -> {
                        binding.loadingLayout1.visibility = View.GONE
                        mapAdapters[MOVIES_COLLECTION_1]?.moviesData = appState.movies
                    }
                    MOVIES_COLLECTION_2 -> {
                        binding.loadingLayout2.visibility = View.GONE
                        mapAdapters[MOVIES_COLLECTION_2]?.moviesData = appState.movies
                    }
                    MOVIES_COLLECTION_3 -> {
                        binding.loadingLayout3.visibility = View.GONE
                        mapAdapters[MOVIES_COLLECTION_3]?.moviesData = appState.movies
                    }
                }
            }

            is AppState.ErrorMovies -> {
                when (appState.key) {
                    MOVIES_COLLECTION_1 -> {
                        Snackbar
                            .make(binding.main,
                                "Error: ${appState.error}",
                                Snackbar.LENGTH_INDEFINITE)
                            .setAction("Reload") {
                                viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_1)
                            }
                            .show()
                    }
                    MOVIES_COLLECTION_2 -> {
                        Snackbar
                            .make(binding.main,
                                "Error: ${appState.error}",
                                Snackbar.LENGTH_INDEFINITE)
                            .setAction("Reload") {
                                viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_2)
                            }
                            .show()
                    }
                    MOVIES_COLLECTION_3 -> {
                        Snackbar
                            .make(binding.main,
                                "Error: ${appState.error}",
                                Snackbar.LENGTH_INDEFINITE)
                            .setAction("Reload") {
                                viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_3)
                            }
                            .show()
                    }
                }

            }
            else -> {
            }
        }
    }


    private fun initRecyclerView() {
        mapRecyclerView = mapOf(
            MOVIES_COLLECTION_1 to binding.recyclerViewHorizontal1,
            MOVIES_COLLECTION_2 to binding.recyclerViewHorizontal2,
            MOVIES_COLLECTION_3 to binding.recyclerViewHorizontal3,
        )

        mapAdapters = mapOf(
            MOVIES_COLLECTION_1 to MoviesAdapter(R.layout.item_for_horizontal),
            MOVIES_COLLECTION_2 to MoviesAdapter(R.layout.item_for_horizontal),
            MOVIES_COLLECTION_3 to MoviesAdapter(R.layout.item_for_horizontal)
        )

        val myListener = MoviesAdapter.OnItemViewClickListener { idMovie ->
            val bundle = Bundle()
            bundle.putInt(MovieFragment.BUNDLE_EXTRA, idMovie)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MovieFragment.newInstance(bundle))
                .addToBackStack(null)
                .commit()
        }

        for (key in mapRecyclerView.keys) {
            mapAdapters[key]?.listener = myListener
            mapRecyclerView[key]?.adapter = mapAdapters[key]
            mapRecyclerView[key]?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            mapRecyclerView[key]?.setHasFixedSize(true);
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        val bundle = Bundle()

        when (v!!.id) {
            R.id.categoryTitleLayout1 -> bundle.putString(MoviesListFragment.BUNDLE_EXTRA,
                MOVIES_COLLECTION_1)
            R.id.categoryTitleLayout2 -> bundle.putString(MoviesListFragment.BUNDLE_EXTRA,
                MOVIES_COLLECTION_2)
            R.id.categoryTitleLayout3 -> bundle.putString(MoviesListFragment.BUNDLE_EXTRA,
                MOVIES_COLLECTION_3)
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, MoviesListFragment.newInstance(bundle))
            .addToBackStack(null)
            .commit()
    }

}