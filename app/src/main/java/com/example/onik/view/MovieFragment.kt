package com.example.onik.view

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.model.room.MovieEntity
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso


class MovieFragment : Fragment() {
    companion object {
        const val BUNDLE_EXTRA: String = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): MovieFragment =
            MovieFragment().apply { arguments = bundle }
    }

    private var menu: Menu? = null
    private var idMovie: Int = 0
    private var movieEntity: MovieEntity = MovieEntity()

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
            movieEntity.idMovie = it

            viewModel.movieDetailsLiveData
                .observe(viewLifecycleOwner, { appState -> renderData(appState) })
            viewModel.getDataFromRemoteSource(idMovie)

            viewModel.getNoteLiveData(idMovie)
                .observe(viewLifecycleOwner, { movieEntity ->
                    movieEntity?.let {
                        this.movieEntity.note = it.note
                        this.movieEntity.favorite = it.favorite
                    }
                    updateAllIcons()
                })
        }
    }


    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()

            is AppState.SuccessMovie -> {
                binding.apply {
                    loadingLayout.hide()
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.movie.poster_path}")
                        .into(posterMini)
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.movie.backdrop_path}")
                        .into(backdrop)
                    title.text = appState.movie.title
                    voteAverage.text =
                        "${appState.movie.vote_average} (${appState.movie.vote_count})"
                    overview.text = appState.movie.overview
                    runtime.text = "${appState.movie.runtime} ${getString(R.string.min)}"
                    releaseDate.text = appState.movie.release_date
                    budget.text = appState.movie.budget.toString()
                    revenue.text = appState.movie.revenue.toString()

                    movieEntity.title = appState.movie.title.toString()
                }

                var genres = ""
                appState.movie.genres?.forEach {
                    genres += "${it.name}, "
                }
                binding.genre.text = genres.dropLast(2)
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
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_note_edit -> {
                showAlertDialogNoteEditClicked()
                return true
            }
            R.id.favorite -> {
                if (movieEntity.favorite == "false") {
                    movieEntity.favorite = "true"
                } else if (movieEntity.favorite == "true") {
                    movieEntity.favorite = "false"
                }

                viewModel.saveNoteToDB(movieEntity)
                updateIconItemFavorite()
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
            setPositiveButton("Сохранить") { _, _ ->
                sendDialogDataToActivity(editText.text.toString())
            }
            setNegativeButton("Отмена") { _, _ -> }
            create()
            show()
        }

        editText.setText(movieEntity.note)
    }


    private fun sendDialogDataToActivity(note: String) {
        movieEntity.note = note
        viewModel.saveNoteToDB(movieEntity)
        updateIconItemActionNoteEdit()
    }


    private fun updateAllIcons() {
        updateIconItemActionNoteEdit()
        updateIconItemFavorite()
    }

    private fun updateIconItemActionNoteEdit() {
        if (movieEntity.note == "") {
            menu?.findItem(R.id.action_note_edit)?.icon = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_baseline_add_comment_24)
            }
        } else {
            menu?.findItem(R.id.action_note_edit)?.icon = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_baseline_comment_24)
            }
        }
    }

    private fun updateIconItemFavorite() {
        if (movieEntity.favorite == "false") {
            menu?.findItem(R.id.favorite)?.icon = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_baseline_favorite_border_24)
            }
        } else if (movieEntity.favorite == "true") {
            menu?.findItem(R.id.favorite)?.icon = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_baseline_favorite_24)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}