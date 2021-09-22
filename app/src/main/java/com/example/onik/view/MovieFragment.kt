package com.example.onik.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.model.data.Movie
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso


class MovieFragment : Fragment() {
    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MovieFragment =
            MovieFragment().apply { arguments = bundle }
    }

    private var movieTemp: Movie? = Movie()

    private var idMovie: Int = 0
    private var _binding: MovieFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by lazy {
        ViewModelProvider(this).get(MovieViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MovieFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.title = "Описание"

        arguments?.getInt(BUNDLE_EXTRA)?.let { it ->
            idMovie = it
            viewModel.movieDetailsLiveData
                .observe(viewLifecycleOwner, { appState -> renderData(appState) })
            viewModel.getDataFromRemoteSource(idMovie)

        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovie -> {
                binding.apply {
                    loadingLayout.hide()
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.movie?.poster_path}")
                        .into(posterMini)
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.movie?.backdrop_path}")
                        .into(backdrop)
                    title.text = appState.movie?.title
                    voteAverage.text =
                        "${appState.movie?.vote_average} (${appState.movie?.vote_count})"
                    overview.text = appState.movie?.overview
                    runtime.text = "${appState.movie?.runtime} ${getString(R.string.min)}"
                    releaseDate.text = appState.movie?.release_date
                    budget.text = appState.movie?.budget.toString()
                    revenue.text = appState.movie?.revenue.toString()
                }

                var genres = ""
                appState.movie?.genres?.forEach {
                    genres += "${it.name}, "
                }
                binding.genre.text = genres.dropLast(2)

                movieTemp = appState.movie
            }

            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.container.showSnackbar(
                    text = appState.error.message!!,
                    action = {
                        viewModel.getDataFromRemoteSource(idMovie)
                    })
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu_movie_fragment, menu)
        menu.findItem(R.id.action_search)?.isVisible = false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                showAlertDialogNoteEditClicked()
                //Toast.makeText(requireActivity(), "Add note 2", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showAlertDialogNoteEditClicked() {
        val customLayout: View = layoutInflater.inflate(R.layout.note_dialog_fragment, null)
        val editText: EditText = customLayout.findViewById(R.id.editText)

        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Редактирование заметки")
            setView(customLayout)
            setPositiveButton("Сохранить") { dialog, which ->
                sendDialogDataToActivity(editText.text.toString())
            }
            setNegativeButton("Отмена") { dialog, which -> }
            create()
            show()
        }

        editText.setText(movieTemp?.note)
    }


    private fun sendDialogDataToActivity(data: String) {
        movieTemp?.let {
            it.note = data
            viewModel.saveMovieToDB(it)
        }
        Toast.makeText(requireActivity(), data, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}