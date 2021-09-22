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
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.CollectionId
import com.example.onik.viewmodel.MoviesCollectionViewModel


class MoviesListFragment : Fragment() {

    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"
        const val BUNDLE_EXTRA_SEARCH: String = "BUNDLE_EXTRA_SEARCH"

        fun newInstance(bundle: Bundle): MoviesListFragment =
            MoviesListFragment().apply { arguments = bundle }
    }

    private var collectionId: CollectionId? = null
    private var searchQuery: String = ""

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
        initRecyclerView()
        setHasOptionsMenu(true)

        arguments?.let { bundle ->
            bundle.getSerializable(BUNDLE_EXTRA)?.let { collectionId = it as CollectionId }
            bundle.getString(BUNDLE_EXTRA_SEARCH)?.let { searchQuery = it }
        }

        collectionId?.let { collectionId ->
            activity?.title = collectionId.description
            viewModel.getMoviesListLiveData(collectionId)
                ?.observe(viewLifecycleOwner, { appState -> renderData(appState) })

            when (collectionId) {
                CollectionId.FIND -> {
                    viewModel.findDataOnRemoteSource(collectionId, searchQuery)
                }
                else -> {
                    viewModel.getDataFromRemoteSource(collectionId)
                }
            }
        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovies -> {
                binding.loadingLayout.hide()
                appState.movies?.results?.let { myAdapter.moviesData = it }
                view?.hideKeyboard()
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(text = appState.error.message.toString(),
                    action = {
                        collectionId?.let { viewModel.getDataFromRemoteSource(it) } //todo
                    })
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
                if (query != null && collectionId != null) {
                    viewModel.findDataOnRemoteSource(collectionId!!, query)
                }
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
            R.id.action_main -> {
                Toast.makeText(requireActivity(), "Fragment 2", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}