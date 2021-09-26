package com.example.onik.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.R
import com.example.onik.databinding.MoviesListFragmentBinding
import com.example.onik.model.data.convertMovieEntityToMovieForCard
import com.example.onik.model.localRepository.ORDER_BY_TITLE
import com.example.onik.model.localRepository.ORDER_BY_TITLE_DESC
import com.example.onik.model.localRepository.ORDER_BY_VOTE
import com.example.onik.model.localRepository.ORDER_BY_VOTE_DESC
import com.example.onik.viewmodel.MoviesFavoritesViewModel


class MoviesFavoritesFragment : Fragment() {

    private var orderBy: String = ORDER_BY_TITLE

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

        subscribeObserver()
    }


    private fun subscribeObserver() {
        viewModel.getAllMovieLocalLiveData(orderBy).observe(viewLifecycleOwner, { listMovieEntity ->
            myAdapter.moviesData = listMovieEntity.map { movieEntity ->
                convertMovieEntityToMovieForCard(movieEntity)
            }
        })
    }


    private fun initRecyclerView() {
        myAdapter.listener = MoviesAdapter.OnItemViewClickListener { movie ->
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_favorites_fragment, menu)
        menu.findItem(R.id.action_search)?.isVisible = false
        menu.findItem(R.id.action_show_favorites)?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_by_title -> {
                orderBy = if (orderBy == ORDER_BY_TITLE) {
                    ORDER_BY_TITLE_DESC
                } else {
                    ORDER_BY_TITLE
                }
                subscribeObserver()
                return true
            }
            R.id.action_sort_by_vote_average -> {
                orderBy = if (orderBy == ORDER_BY_VOTE) ORDER_BY_VOTE_DESC
                else ORDER_BY_VOTE

                subscribeObserver()
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