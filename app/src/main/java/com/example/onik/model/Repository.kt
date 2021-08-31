package com.example.onik.model

interface Repository {
    fun getMovieFromServer(): Movie
    fun getMovieFromLocalStorage(): Movie

    fun getPopularMoviesFromServer(): Array<Movie>
    fun getPopularMoviesFromLocalStorage(): Array<Movie>
}