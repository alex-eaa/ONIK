package com.example.onik.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.databinding.MainFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MoviesCollectionViewModel
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_1
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_2
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_3
import com.example.onik.viewmodel.Constants.Companion.MOVIES_COLLECTION_4
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesCollectionViewModel by lazy {
        ViewModelProvider(this).get(MoviesCollectionViewModel::class.java)
    }

    private val mapAdapters: Map<String, MoviesAdapter> by lazy {
        mapOf(
            MOVIES_COLLECTION_1 to MoviesAdapter(R.layout.item_for_horizontal),
            MOVIES_COLLECTION_2 to MoviesAdapter(R.layout.item_for_horizontal),
            MOVIES_COLLECTION_3 to MoviesAdapter(R.layout.item_for_horizontal),
            MOVIES_COLLECTION_4 to MoviesAdapter(R.layout.item_for_horizontal),
        )
    }


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
        binding.categoryTitleLayout4.setOnClickListener(this)

        with(viewModel) {
            getMoviesListLiveData(MOVIES_COLLECTION_1)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, MOVIES_COLLECTION_1) })

            getMoviesListLiveData(MOVIES_COLLECTION_2)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, MOVIES_COLLECTION_2) })

            getMoviesListLiveData(MOVIES_COLLECTION_3)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, MOVIES_COLLECTION_3) })

            getMoviesListLiveData(MOVIES_COLLECTION_4)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, MOVIES_COLLECTION_4) })

            getDataFromRemoteSource(MOVIES_COLLECTION_1)
            getDataFromRemoteSource(MOVIES_COLLECTION_2)
            getDataFromRemoteSource(MOVIES_COLLECTION_3)
            getDataFromRemoteSource(MOVIES_COLLECTION_4)
        }
    }


    private fun renderData(appState: AppState, collectionName: String) {
        when (appState) {
            is AppState.Loading -> when (collectionName) {
                MOVIES_COLLECTION_1 -> binding.loadingLayout1.show()
                MOVIES_COLLECTION_2 -> binding.loadingLayout2.show()
                MOVIES_COLLECTION_3 -> binding.loadingLayout3.show()
                MOVIES_COLLECTION_4 -> binding.loadingLayout4.show()
            }

            is AppState.SuccessMovies -> when (collectionName) {
                MOVIES_COLLECTION_1 -> {
                    binding.loadingLayout1.hide()
                    appState.movies?.results?.let {
                        mapAdapters[MOVIES_COLLECTION_1]?.moviesData = it
                    }
                }
                MOVIES_COLLECTION_2 -> {
                    binding.loadingLayout2.hide()
                    appState.movies?.results?.let {
                        mapAdapters[MOVIES_COLLECTION_2]?.moviesData = it
                    }
                }
                MOVIES_COLLECTION_3 -> {
                    binding.loadingLayout3.hide()
                    appState.movies?.results?.let {
                        mapAdapters[MOVIES_COLLECTION_3]?.moviesData = it
                    }
                }
                MOVIES_COLLECTION_4 -> {
                    binding.loadingLayout4.hide()
                    appState.movies?.results?.let {
                        mapAdapters[MOVIES_COLLECTION_4]?.moviesData = it
                    }
                }
            }

            is AppState.Error -> {
                when (collectionName) {
                    MOVIES_COLLECTION_1 -> binding.loadingLayout1.hide()
                    MOVIES_COLLECTION_2 -> binding.loadingLayout2.hide()
                    MOVIES_COLLECTION_3 -> binding.loadingLayout3.hide()
                    MOVIES_COLLECTION_4 -> binding.loadingLayout4.hide()
                }

                binding.container.showSnackbar(
                    action = {
                        viewModel.apply {
                            getDataFromRemoteSource(MOVIES_COLLECTION_1)
                            getDataFromRemoteSource(MOVIES_COLLECTION_2)
                            getDataFromRemoteSource(MOVIES_COLLECTION_3)
                            getDataFromRemoteSource(MOVIES_COLLECTION_4)
                        }
                    })
            }

            else -> {}
        }
    }


    private fun initRecyclerView() {
        val mapRecyclerView: Map<String, RecyclerView> = mapOf(
            MOVIES_COLLECTION_1 to binding.recyclerViewHorizontal1,
            MOVIES_COLLECTION_2 to binding.recyclerViewHorizontal2,
            MOVIES_COLLECTION_3 to binding.recyclerViewHorizontal3,
            MOVIES_COLLECTION_4 to binding.recyclerViewHorizontal4,
        )

        for (key in mapRecyclerView.keys) {
            mapAdapters[key]?.listener = MoviesAdapter.OnItemViewClickListener { movie ->
                activity?.supportFragmentManager?.let { fragmentManager ->
                    val bundle = Bundle()
                    bundle.putInt(MovieFragment.BUNDLE_EXTRA, movie.id!!)
                    fragmentManager.beginTransaction()
                        .replace(R.id.container, MovieFragment.newInstance(bundle))
                        .addToBackStack(null)
                        .commit()
                }
            }

            mapRecyclerView[key]?.apply {
                adapter = mapAdapters[key]
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true);
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View) {
        if (v.id == R.id.categoryTitleLayout1
            || v.id == R.id.categoryTitleLayout2
            || v.id == R.id.categoryTitleLayout3
            || v.id == R.id.categoryTitleLayout4
        ) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesListFragment.newInstance(Bundle().apply {
                    when (v.id) {
                        R.id.categoryTitleLayout1 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_1)
                        R.id.categoryTitleLayout2 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_2)
                        R.id.categoryTitleLayout3 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_3)
                        R.id.categoryTitleLayout4 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_4)
                    }
                }))
                .addToBackStack(null)
                .commit()
        }
    }

}