package com.example.onik.view

import android.os.Bundle
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
            MOVIES_COLLECTION_3 to MoviesAdapter(R.layout.item_for_horizontal)
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

        with(viewModel) {
            getMoviesListLiveData(MOVIES_COLLECTION_1)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState) })

            getMoviesListLiveData(MOVIES_COLLECTION_2)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState) })

            getMoviesListLiveData(MOVIES_COLLECTION_3)?.observe(viewLifecycleOwner,
                { appState -> renderData(appState) })

            getDataFromRemoteSource(MOVIES_COLLECTION_1)
            getDataFromRemoteSource(MOVIES_COLLECTION_2)
            getDataFromRemoteSource(MOVIES_COLLECTION_3)
        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.LoadingMovies -> when (appState.key) {
                MOVIES_COLLECTION_1 -> binding.loadingLayout1.show()
                MOVIES_COLLECTION_2 -> binding.loadingLayout2.show()
                MOVIES_COLLECTION_3 -> binding.loadingLayout3.show()
            }

            is AppState.SuccessMovies -> when (appState.key) {
                MOVIES_COLLECTION_1 -> {
                    binding.loadingLayout1.hide()
                    mapAdapters[MOVIES_COLLECTION_1]?.moviesData = appState.movies
                }
                MOVIES_COLLECTION_2 -> {
                    binding.loadingLayout2.hide()
                    mapAdapters[MOVIES_COLLECTION_2]?.moviesData = appState.movies
                }
                MOVIES_COLLECTION_3 -> {
                    binding.loadingLayout3.hide()
                    mapAdapters[MOVIES_COLLECTION_3]?.moviesData = appState.movies
                }
            }

            is AppState.Error -> {
                binding.loadingLayout1.hide()
                binding.loadingLayout2.hide()
                binding.loadingLayout3.hide()

                binding.container.showSnackbar("Коллекции не удалось загрузить.",
                    "Повторить",
                    action = {
                        viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_1)
                        viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_2)
                        viewModel.getDataFromRemoteSource(MOVIES_COLLECTION_3)
                    })
            }
        }
    }


    private fun initRecyclerView() {
        val mapRecyclerView: Map<String, RecyclerView> = mapOf(
            MOVIES_COLLECTION_1 to binding.recyclerViewHorizontal1,
            MOVIES_COLLECTION_2 to binding.recyclerViewHorizontal2,
            MOVIES_COLLECTION_3 to binding.recyclerViewHorizontal3,
        )

        for (key in mapRecyclerView.keys) {
            mapAdapters[key]?.listener = MoviesAdapter.OnItemViewClickListener { movie ->
                activity?.supportFragmentManager?.let { fragmentManager ->
                    val bundle = Bundle()
                    bundle.putInt(MovieFragment.BUNDLE_EXTRA, movie.id)
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
        if (v.id == R.id.categoryTitleLayout1 || v.id == R.id.categoryTitleLayout2 || v.id == R.id.categoryTitleLayout3) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesListFragment.newInstance(Bundle().apply {
                    when (v.id) {
                        R.id.categoryTitleLayout1 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_1)
                        R.id.categoryTitleLayout2 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_2)
                        R.id.categoryTitleLayout3 -> putString(MoviesListFragment.BUNDLE_EXTRA,
                            MOVIES_COLLECTION_3)
                    }
                }))
                .addToBackStack(null)
                .commit()
        }
    }

}