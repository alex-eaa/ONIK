package com.example.onik.viewmodel

import com.example.onik.model.ListMoviesDTO
import com.example.onik.model.MovieDTO

sealed class AppState {
    data class SuccessMovie(val movie: MovieDTO?) : AppState()
    data class SuccessMovies(val movies: ListMoviesDTO?) : AppState()
    data class Success<T>(val t: T?) : AppState()
    data class Error(val error: Throwable) : AppState()

    object Loading : AppState()
}
