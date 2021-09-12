package com.example.onik.viewmodel

import com.example.onik.model.ListMoviesDTO
import com.example.onik.model.MovieDTO

sealed class AppState{
    data class SuccessMovie(val movie: MovieDTO) : AppState()
    data class SuccessMovies(val movies: ListMoviesDTO, val key: String) : AppState()

    data class Error(val error: Throwable) : AppState()
    data class ErrorMovies(val error: Throwable, val key: String) : AppState()

    data class LoadingMovies(val key: String = "") : AppState()
    object Loading : AppState()
}
