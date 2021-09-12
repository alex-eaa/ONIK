package com.example.onik.viewmodel

import com.example.onik.model.ListMoviesDTO
import com.example.onik.model.MovieDTO

sealed class AppState{
    data class SuccessMovie(val movie: MovieDTO?) : AppState()
    data class SuccessMovies(val movies: ListMoviesDTO?) : AppState()
    data class Error(val error: Throwable) : AppState()

    data class LoadingMovies(val key: String = "") : AppState()
    object Loading : AppState()
}
