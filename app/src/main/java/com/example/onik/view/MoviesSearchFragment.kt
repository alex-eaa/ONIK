package com.example.onik.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.R
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.databinding.MoviesSearchFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.CollectionId
import com.example.onik.viewmodel.MoviesCollectionViewModel
import com.example.onik.viewmodel.MoviesSearchViewModel


class MoviesSearchFragment : Fragment() {

    companion object {
        const val BUNDLE_SEARCH_QUERY_EXTRA: String = "BUNDLE_SEARCH_QUERY_EXTRA"

        fun newInstance(bundle: Bundle): MoviesSearchFragment =
            MoviesSearchFragment().apply { arguments = bundle }
    }

    private var searchQuery: String = ""

    private var _binding: MoviesSearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesSearchViewModel by lazy {
        ViewModelProvider(this).get(MoviesSearchViewModel::class.java)
    }

    private val myAdapter: MoviesAdapter by lazy { MoviesAdapter(R.layout.item) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MoviesSearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setHasOptionsMenu(true)

        arguments?.let { bundle ->
            bundle.getString(BUNDLE_SEARCH_QUERY_EXTRA)?.let { searchQuery = it }
        }

        activity?.title = resources.getString(R.string.title_find)
        viewModel.moviesListLiveData.observe(viewLifecycleOwner,
            { appState -> renderData(appState) })

        viewModel.findDataOnRemoteSource(searchQuery)
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovies -> {
                binding.loadingLayout.hide()
                appState.movies.results?.let { myAdapter.moviesData = it }
                view?.hideKeyboard()
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(text = appState.error.message.toString(),
                    action = { viewModel.findDataOnRemoteSource(searchQuery) }
                )
            }
        }
    }


    private fun initRecyclerView() {
        myAdapter.listener = MoviesAdapter.OnItemViewClickListener { movie ->
            activity?.supportFragmentManager?.let { fragmentManager ->
                fragmentManager.beginTransaction()
                    .replace(R.id.container, MovieFragment.newInstance(Bundle().apply {
                        putInt(MovieFragment.BUNDLE_EXTRA, movie.id!!)
                    }))
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.mainRecyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchText: SearchView? = menu.findItem(R.id.action_search)?.actionView as SearchView?
        searchText?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.findDataOnRemoteSource(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        super.onPrepareOptionsMenu(menu)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}