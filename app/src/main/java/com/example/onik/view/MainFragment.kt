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
import com.example.onik.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment(), View.OnClickListener {

    companion object {
        const val KEY_RECYCLER_1 = "RECYCLER_1"
        const val KEY_RECYCLER_2 = "RECYCLER_2"
        const val KEY_RECYCLER_3 = "RECYCLER_3"

        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
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

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getMoviesListLiveData1().observe(viewLifecycleOwner, observer)
        viewModel.getMoviesListLiveData2().observe(viewLifecycleOwner, observer)
        viewModel.getMoviesListLiveData3().observe(viewLifecycleOwner, observer)

        viewModel.getDataFromRemoteSource()
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> {
                binding.loadingLayout1.visibility = View.VISIBLE
                binding.loadingLayout2.visibility = View.VISIBLE
                binding.loadingLayout3.visibility = View.VISIBLE
            }

            is AppState.SuccessMovies1 -> {
                binding.loadingLayout1.visibility = View.GONE
                mapAdapters[KEY_RECYCLER_1]?.moviesData = appState.movies
            }

            is AppState.SuccessMovies2 -> {
                binding.loadingLayout2.visibility = View.GONE
                mapAdapters[KEY_RECYCLER_2]?.moviesData = appState.movies
            }

            is AppState.SuccessMovies3 -> {
                binding.loadingLayout3.visibility = View.GONE
                mapAdapters[KEY_RECYCLER_3]?.moviesData = appState.movies
            }

            is AppState.Error -> {
                binding.loadingLayout1.visibility = View.GONE
                binding.loadingLayout2.visibility = View.GONE
                binding.loadingLayout3.visibility = View.GONE
                Snackbar
                    .make(binding.main, "Error: ${appState.error}", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") {
                        viewModel.getDataFromRemoteSource()
                    }
                    .show()
            }
            else -> {
            }
        }
    }


    private fun initRecyclerView() {
        mapRecyclerView = mapOf(
            KEY_RECYCLER_1 to binding.recyclerViewHorizontal1,
            KEY_RECYCLER_2 to binding.recyclerViewHorizontal2,
            KEY_RECYCLER_3 to binding.recyclerViewHorizontal3,
        )

        mapAdapters = mapOf(
            KEY_RECYCLER_1 to MoviesAdapter(R.layout.item_for_horizontal),
            KEY_RECYCLER_2 to MoviesAdapter(R.layout.item_for_horizontal),
            KEY_RECYCLER_3 to MoviesAdapter(R.layout.item_for_horizontal)
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
            R.id.categoryTitleLayout1 -> bundle.putInt(MoviesListFragment.BUNDLE_EXTRA, 1)
            R.id.categoryTitleLayout2 -> bundle.putInt(MoviesListFragment.BUNDLE_EXTRA, 2)
            R.id.categoryTitleLayout3 -> bundle.putInt(MoviesListFragment.BUNDLE_EXTRA, 3)
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, MoviesListFragment.newInstance(bundle))
            .addToBackStack(null)
            .commit()
    }

}