package com.example.onik.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.R
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.CollectionId
import com.example.onik.viewmodel.MoviesCollectionViewModel
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MoviesListFragment : Fragment() {

    companion object {
        private const val TAG = "MoviesListFragment"
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MoviesListFragment =
            MoviesListFragment().apply { arguments = bundle }
    }

    private var collectionId: CollectionId? = null

    private var _binding: MoviesListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesCollectionViewModel by lazy {
        ViewModelProvider(this).get(MoviesCollectionViewModel::class.java)

    }

    private val myAdapter: MoviesAdapterForPaging by lazy { MoviesAdapterForPaging(R.layout.item) }


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
        }

        collectionId?.let { collectionId ->
            activity?.title = collectionId.description

//            viewModel.getMoviesListLiveData(collectionId)
//                ?.observe(viewLifecycleOwner, { appState -> renderData(appState) })

//            viewModel.moviesLiveData.observe(viewLifecycleOwner, {
//                myAdapter.submitData(lifecycle, it)
//            })

//            viewModel.getOneCollectionCoroutines(collectionId, 1)


            lifecycleScope.launch{
                viewModel.moviesFlow.collectLatest(myAdapter::submitData)
            }

        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovies -> {
                binding.loadingLayout.hide()
//                myAdapter.moviesData = appState.movies
                view?.hideKeyboard()
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(text = appState.error.message.toString(),
                    action = { view ->
                        collectionId?.let {
                            lifecycleScope.launch{
                                viewModel.moviesFlow.collectLatest(myAdapter::submitData)
                            }
                        }
                    })
            }
        }
    }


    private fun initRecyclerView() {
        myAdapter.listener = MoviesAdapterForPaging.OnItemViewClickListener { movie ->
            activity?.supportFragmentManager?.let { fragmentManager ->
                fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}