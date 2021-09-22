package com.example.onik.model.localRepository

import com.example.onik.model.data.Movie

interface LocalRepository {
    fun getAllMovie(): List<Movie>
    fun getMovieNote(movieId: Int): String
    fun saveMovie(movie: Movie)
}