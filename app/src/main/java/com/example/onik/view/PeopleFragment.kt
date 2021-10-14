package com.example.onik.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.onik.R
import com.example.onik.databinding.PeopleFragmentBinding
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.PeopleViewModel
import com.squareup.picasso.Picasso

const val TAGG = "PeopleFragment"

class PeopleFragment : Fragment() {
    companion object {
        const val BUNDLE_EXTRA_PEOPLE_ID: String = "BUNDLE_EXTRA_PEOPLE_ID"

        fun newInstance(bundle: Bundle): PeopleFragment =
            PeopleFragment().apply { arguments = bundle }
    }

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
        initView()

        arguments?.getInt(BUNDLE_EXTRA_PEOPLE_ID)?.let { idPeople ->
            this.idPeople = idPeople

            viewModel.peopleLiveData
                .observe(viewLifecycleOwner, { appState -> renderDataPeople(appState) })
            viewModel.movieCreditsLiveData
                .observe(viewLifecycleOwner, { appState -> renderDataMovieCredits(appState) })

            viewModel.getDataFromRemoteSource(idPeople)

        }
    }

    private fun initView() {
        setHasOptionsMenu(true)
        initRecyclerView()

        binding.peoplePlaceOfBirth.setOnClickListener { view ->
            val address = binding.peoplePlaceOfBirth.text
            if (!address.isNullOrEmpty()) {
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.container, GoogleMapsFragment.newInstance(Bundle().apply {
                            putString(GoogleMapsFragment.BUNDLE_EXTRA_ADDRESS, address.toString())
                        }))
                        .addToBackStack(null)
                        .commit()
                }
            }
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
                    myAdapter.moviesData = appState.movies
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