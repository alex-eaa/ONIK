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
import com.example.onik.viewmodel.CollectionId
import com.example.onik.viewmodel.MoviesCollectionViewModel


class MainFragment : Fragment(), View.OnClickListener {

    companion object {
        private const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesCollectionViewModel by lazy {
        ViewModelProvider(this).get(MoviesCollectionViewModel::class.java)
    }

    private val mapAdapters: Map<CollectionId, MoviesAdapter> by lazy {
        mapOf(
            CollectionId.POPULAR to MoviesAdapter(R.layout.item_for_horizontal),
            CollectionId.TOP_RATED to MoviesAdapter(R.layout.item_for_horizontal),
            CollectionId.NOW_PLAYING to MoviesAdapter(R.layout.item_for_horizontal),
            CollectionId.UPCOMING to MoviesAdapter(R.layout.item_for_horizontal),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        with(viewModel) {
            listCollectionId.clear()
            listCollectionId.add(CollectionId.POPULAR)
            listCollectionId.add(CollectionId.TOP_RATED)
            listCollectionId.add(CollectionId.NOW_PLAYING)
            listCollectionId.add(CollectionId.UPCOMING)
            getAllCollections()
        }
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
        activity?.title = getString(R.string.app_name)
        initRecyclerView()

        binding.categoryTitleLayout1.setOnClickListener(this)
        binding.categoryTitleLayout2.setOnClickListener(this)
        binding.categoryTitleLayout3.setOnClickListener(this)
        binding.categoryTitleLayout4.setOnClickListener(this)

        with(viewModel) {

            getMoviesListLiveData(CollectionId.POPULAR)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, CollectionId.POPULAR) })

            getMoviesListLiveData(CollectionId.TOP_RATED)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, CollectionId.TOP_RATED) })

            getMoviesListLiveData(CollectionId.NOW_PLAYING)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, CollectionId.NOW_PLAYING) })

            getMoviesListLiveData(CollectionId.UPCOMING)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState, CollectionId.UPCOMING) })
        }
    }


    private fun renderData(appState: AppState, collectionName: CollectionId) {
        when (appState) {
            is AppState.Loading -> when (collectionName) {
                CollectionId.POPULAR -> binding.loadingLayout1.show()
                CollectionId.TOP_RATED -> binding.loadingLayout2.show()
                CollectionId.NOW_PLAYING -> binding.loadingLayout3.show()
                CollectionId.UPCOMING -> binding.loadingLayout4.show()
            }

            is AppState.SuccessMovies -> when (collectionName) {
                CollectionId.POPULAR -> {
                    binding.loadingLayout1.hide()
                    mapAdapters[CollectionId.POPULAR]?.moviesData = appState.movies
                }
                CollectionId.TOP_RATED -> {
                    binding.loadingLayout2.hide()
                    mapAdapters[CollectionId.TOP_RATED]?.moviesData = appState.movies
                }
                CollectionId.NOW_PLAYING -> {
                    binding.loadingLayout3.hide()
                    mapAdapters[CollectionId.NOW_PLAYING]?.moviesData = appState.movies
                }
                CollectionId.UPCOMING -> {
                    binding.loadingLayout4.hide()
                    mapAdapters[CollectionId.UPCOMING]?.moviesData = appState.movies
                }
            }

            is AppState.Error -> {
                when (collectionName) {
                    CollectionId.POPULAR -> binding.loadingLayout1.hide()
                    CollectionId.TOP_RATED -> binding.loadingLayout2.hide()
                    CollectionId.NOW_PLAYING -> binding.loadingLayout3.hide()
                    CollectionId.UPCOMING -> binding.loadingLayout4.hide()
                }

                Log.d("zzz", appState.error.message!!)
                binding.container.showSnackbar(action = {
                    viewModel.apply {
                        viewModel.getAllCollections()
                    }
                })
            }

            else -> {
            }
        }
    }


    private fun initRecyclerView() {
        val mapRecyclerView: Map<CollectionId, RecyclerView> = mapOf(
            CollectionId.POPULAR to binding.recyclerViewHorizontal1,
            CollectionId.TOP_RATED to binding.recyclerViewHorizontal2,
            CollectionId.NOW_PLAYING to binding.recyclerViewHorizontal3,
            CollectionId.UPCOMING to binding.recyclerViewHorizontal4,
        )

        for (key in mapRecyclerView.keys) {
            mapAdapters[key]?.listener = MoviesAdapter.OnItemViewClickListener { movie ->
                activity?.supportFragmentManager?.let { fragmentManager ->
                    val bundle = Bundle()
                    bundle.putInt(MovieFragment.BUNDLE_EXTRA, movie.id!!)
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
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
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.container, MoviesListFragment.newInstance(Bundle().apply {
                    when (v.id) {
                        R.id.categoryTitleLayout1 -> putSerializable(
                            MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.POPULAR
                        )
                        R.id.categoryTitleLayout2 -> putSerializable(
                            MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.TOP_RATED
                        )
                        R.id.categoryTitleLayout3 -> putSerializable(
                            MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.NOW_PLAYING
                        )
                        R.id.categoryTitleLayout4 -> putSerializable(
                            MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.UPCOMING
                        )
                    }
                }))
                .addToBackStack(null)
                .commit()
        }
    }

}