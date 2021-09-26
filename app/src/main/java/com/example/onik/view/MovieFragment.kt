package com.example.onik.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.onik.R
import com.example.onik.databinding.MovieFragmentBinding
import com.example.onik.model.data.Movie
import com.example.onik.model.data.MovieLocal
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
    private var movieLocal: MovieLocal = MovieLocal()

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
        activity?.title = resources.getString(R.string.title_details)

        arguments?.getInt(BUNDLE_EXTRA)?.let { idMovie ->
            movieLocal.idMovie = idMovie

            viewModel.movieDetailsLiveData
                .observe(viewLifecycleOwner, { appState -> renderData(appState) })
            viewModel.getDataFromRemoteSource(idMovie)

            viewModel.getNoteLiveData(idMovie)
                .observe(viewLifecycleOwner, { movieEntity ->
                    movieEntity?.let {
                        this.movieLocal.note = it.note
                        this.movieLocal.favorite = it.favorite.toBoolean()
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
                    val gradientColor = binding.container.background as ColorDrawable
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.movie.poster_path}")
                        .into(posterMini)
                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${appState.movie.backdrop_path}")
                        .transform(GradientTransformation(gradientColor.color))
                        .into(backdrop)

                    title.text = appState.movie.title
                    voteAverage.text =
                        "${appState.movie.vote_average} (${appState.movie.vote_count})"
                    overview.text = appState.movie.overview
                    runtime.text = "${appState.movie.runtime} ${getString(R.string.min)}"
                    releaseDate.text = appState.movie.release_date
                    budget.text = appState.movie.budget.toString()
                    revenue.text = appState.movie.revenue.toString()

                    updateMovieLocalWithMovie(appState.movie)
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
                        viewModel.getDataFromRemoteSource(movieLocal.idMovie)
                    })
            }
        }
    }


    private fun updateMovieLocalWithMovie(movie: Movie) {
        movieLocal.apply {
            title = movie.title.toString()
            poster_path = movie.poster_path.toString()
            release_date = movie.release_date.toString()
            movie.vote_average?.let { vote_average = it }
            if (favorite || note != "") {
                viewModel.saveNoteToDB(movieLocal)
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
            R.id.action_to_favorite -> {
                movieLocal.favorite = !movieLocal.favorite

                viewModel.saveNoteToDB(movieLocal)
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

        editText.setText(movieLocal.note)
    }


    private fun sendDialogDataToActivity(note: String) {
        movieLocal.note = note
        viewModel.saveNoteToDB(movieLocal)
        updateIconItemActionNoteEdit()
    }


    private fun updateAllIcons() {
        updateIconItemActionNoteEdit()
        updateIconItemFavorite()
    }

    private fun updateIconItemActionNoteEdit() {
        if (movieLocal.note == "") {
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
        if (!movieLocal.favorite) {
            menu?.findItem(R.id.action_to_favorite)?.icon = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_baseline_favorite_border_24)
            }
        } else if (movieLocal.favorite) {
            menu?.findItem(R.id.action_to_favorite)?.icon = activity?.let {
                ContextCompat.getDrawable(it, R.drawable.ic_baseline_favorite_24)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}