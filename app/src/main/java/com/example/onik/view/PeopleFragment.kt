package com.example.onik.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.databinding.PeopleFragmentBinding
import com.example.onik.model.data.Movie
import com.example.onik.model.data.MovieLocal
import com.example.onik.model.room.MovieEntity
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MovieViewModel
import com.example.onik.viewmodel.PeopleViewModel
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

const val TAGG = "PeopleFragment"

class PeopleFragment : Fragment() {
    companion object {
        const val BUNDLE_EXTRA_PEOPLE_ID: String = "BUNDLE_EXTRA_PEOPLE_ID"

        fun newInstance(bundle: Bundle): PeopleFragment =
            PeopleFragment().apply { arguments = bundle }
    }

    private var menu: Menu? = null
    private var idPeople = 0

    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PeopleViewModel by lazy {
        ViewModelProvider(this).get(PeopleViewModel::class.java)
    }

    private val myAdapter: MoviesAdapter by lazy { MoviesAdapter(R.layout.item) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.title = resources.getString(R.string.title_details)
        initRecyclerView()

        arguments?.getInt(BUNDLE_EXTRA_PEOPLE_ID)?.let { idPeople ->
            this.idPeople = idPeople

            viewModel.peopleLiveData
                .observe(viewLifecycleOwner, { appState -> renderDataPeople(appState) })
            viewModel.movieCreditsLiveData
                .observe(viewLifecycleOwner, { appState -> renderDataMovieCredits(appState) })

            viewModel.getDataFromRemoteSource(idPeople)

        }
    }


    private fun renderDataPeople(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessCast -> {
                binding.apply {
                    loadingLayout.hide()

                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.cast.profile_path}")
                        .into(peoplePhoto)

                    peopleName.text = appState.cast.name
                    peopleBirthday.text = appState.cast.birthday
                    peoplePlaceOfBirth.text = appState.cast.place_of_birth
                }
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(
                    text = appState.error.message!!,
                    action = {
                        viewModel.getDataFromRemoteSource(idPeople)
                    })
            }
        }
    }


    private fun renderDataMovieCredits(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovies -> {
                binding.apply {
                    loadingLayout.hide()
                    appState.movies.results?.let { myAdapter.moviesData = it }
                }
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(
                    text = appState.error.message!!,
                    action = {
                        viewModel.getDataFromRemoteSource(idPeople)
                    })
            }
        }
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

        binding.castRecyclerView.apply {
            adapter = myAdapter
            setHasFixedSize(true)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}