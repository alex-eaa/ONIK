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
import com.example.onik.Foo.Companion.movies
import com.example.onik.R
import com.example.onik.databinding.MainFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter1: MoviesAdapter
    private lateinit var adapter2: MoviesAdapter
    private lateinit var adapter3: MoviesAdapter
    private lateinit var mainRecyclerView1: RecyclerView
    private lateinit var mainRecyclerView2: RecyclerView
    private lateinit var mainRecyclerView3: RecyclerView


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

        binding.categoryTitleLayout1.setOnClickListener (this)
        binding.categoryTitleLayout2.setOnClickListener (this)
        binding.categoryTitleLayout3.setOnClickListener (this)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { appState -> renderData(appState) }
        viewModel.getMoviesListLiveData1().observe(viewLifecycleOwner, observer)
        viewModel.getMoviesListLiveData2().observe(viewLifecycleOwner, observer)
        viewModel.getMoviesListLiveData3().observe(viewLifecycleOwner, observer)

        viewModel.getDataFromRemoteSource()
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE

            is AppState.SuccessMovies1 -> {
                binding.loadingLayout.visibility = View.GONE
                adapter1.setData(appState.movies)
            }

            is AppState.SuccessMovies2 -> {
                binding.loadingLayout.visibility = View.GONE
                adapter2.setData(appState.movies)
            }

            is AppState.SuccessMovies3 -> {
                binding.loadingLayout.visibility = View.GONE
                adapter3.setData(appState.movies)
            }

            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
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
        mainRecyclerView1 = binding.recyclerViewHorizontal1
        adapter1 =
            MoviesAdapter(R.layout.item_for_horizontal) { position -> onListItemClick(position) }
        mainRecyclerView1.adapter = adapter1
        mainRecyclerView1.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mainRecyclerView1.setHasFixedSize(true);

        mainRecyclerView2 = binding.recyclerViewHorizontal2
        adapter2 =
            MoviesAdapter(R.layout.item_for_horizontal) { position -> onListItemClick(position) }
        mainRecyclerView2.adapter = adapter2
        mainRecyclerView2.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mainRecyclerView2.setHasFixedSize(true);

        mainRecyclerView3 = binding.recyclerViewHorizontal3
        adapter3 =
            MoviesAdapter(R.layout.item_for_horizontal) { position -> onListItemClick(position) }
        mainRecyclerView3.adapter = adapter3
        mainRecyclerView3.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mainRecyclerView3.setHasFixedSize(true);
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