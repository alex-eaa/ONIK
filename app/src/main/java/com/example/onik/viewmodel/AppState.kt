package com.example.onik.viewmodel

import com.example.onik.model.data.Cast
import com.example.onik.model.data.ListCasts
import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.Movie

sealed class AppState {
    data class SuccessMovie(val movie: Movie) : AppState()
    data class SuccessMovies(val movies: List<Movie>) : AppState()
    data class SuccessCasts(val casts: ListCasts) : AppState()
    data class SuccessCast(val cast: Cast) : AppState()
    data class Success<T>(val t: T?) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class ErrorMessage(val message: String?) : AppState()

    object Loading : AppState()
}
