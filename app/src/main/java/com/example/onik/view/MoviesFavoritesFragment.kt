package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.R
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.model.data.convertMovieEntityToMovieForCard
import com.example.onik.viewmodel.MoviesFavoritesViewModel


class MoviesFavoritesFragment : Fragment() {

    private var _binding: MoviesListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesFavoritesViewModel by lazy {
        ViewModelProvider(this).get(MoviesFavoritesViewModel::class.java)
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
        activity?.title = resources.getString(R.string.title_favorites)

        viewModel.getAllMovieLocalLiveData().observe(viewLifecycleOwner, { listMovieEntity ->
            myAdapter.moviesData  = listMovieEntity.map { movieEntity ->
                convertMovieEntityToMovieForCard(movieEntity)
            }
        })
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
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MoviesSearchFragment.newInstance(Bundle().apply {
                        putString(MoviesSearchFragment.BUNDLE_SEARCH_QUERY_EXTRA, query)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}