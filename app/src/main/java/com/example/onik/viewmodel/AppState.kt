package com.example.onik.viewmodel

import com.example.onik.model.Cast
import com.example.onik.model.Movie

sealed class AppState{
    data class SuccessMovie(val movie: Movie) : AppState()
    data class SuccessMovies(val movies: List<Movie>, val key: String) : AppState()

    data class Error(val error: Throwable) : AppState()
    data class ErrorMovies(val error: Throwable, val key: String) : AppState()

    data class LoadingMovies(val key: String = "") : AppState()
    object Loading : AppState()
}
