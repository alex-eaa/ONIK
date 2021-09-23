package com.example.onik.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

            getDataFromRemoteSource(CollectionId.POPULAR)
            getDataFromRemoteSource(CollectionId.TOP_RATED)
            getDataFromRemoteSource(CollectionId.NOW_PLAYING)
            getDataFromRemoteSource(CollectionId.UPCOMING)
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
                    appState.movies?.results?.let {
                        mapAdapters[CollectionId.POPULAR]?.moviesData = it
                    }
                }
                CollectionId.TOP_RATED -> {
                    binding.loadingLayout2.hide()
                    appState.movies?.results?.let {
                        mapAdapters[CollectionId.TOP_RATED]?.moviesData = it
                    }
                }
                CollectionId.NOW_PLAYING -> {
                    binding.loadingLayout3.hide()
                    appState.movies?.results?.let {
                        mapAdapters[CollectionId.NOW_PLAYING]?.moviesData = it
                    }
                }
                CollectionId.UPCOMING -> {
                    binding.loadingLayout4.hide()
                    appState.movies?.results?.let {
                        mapAdapters[CollectionId.UPCOMING]?.moviesData = it
                    }
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
                        getDataFromRemoteSource(CollectionId.POPULAR)
                        getDataFromRemoteSource(CollectionId.TOP_RATED)
                        getDataFromRemoteSource(CollectionId.NOW_PLAYING)
                        getDataFromRemoteSource(CollectionId.UPCOMING)
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
                        R.id.categoryTitleLayout1 -> putSerializable(MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.POPULAR)
                        R.id.categoryTitleLayout2 -> putSerializable(MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.TOP_RATED)
                        R.id.categoryTitleLayout3 -> putSerializable(MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.NOW_PLAYING)
                        R.id.categoryTitleLayout4 -> putSerializable(MoviesListFragment.BUNDLE_EXTRA,
                            CollectionId.UPCOMING)
                    }
                }))
                .addToBackStack(null)
                .commit()
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchText: SearchView? = menu.findItem(R.id.action_search)?.actionView as SearchView?
        searchText?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MoviesListFragment.newInstance(Bundle().apply {
                        putSerializable(MoviesListFragment.BUNDLE_EXTRA, CollectionId.FIND)
                        putString(MoviesListFragment.BUNDLE_EXTRA_SEARCH, query)
                    }))
                    .addToBackStack(null)
                    .commit()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.action_main -> {
//                Toast.makeText(requireActivity(), "Fragment", Toast.LENGTH_LONG).show()
//                return true
//            }
        }
        return super.onOptionsItemSelected(item)
    }

}