package com.example.onik.viewmodel

import com.example.onik.model.Cast
import com.example.onik.model.Movie

sealed class AppState{
    data class SuccessMovie(val movie: Movie) : AppState()
    data class SuccessMovies(val movies: List<Movie>) : AppState()
    data class SuccessMovies1(val movies: List<Movie>) : AppState()
    data class SuccessMovies2(val movies: List<Movie>) : AppState()
    data class SuccessMovies3(val movies: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
