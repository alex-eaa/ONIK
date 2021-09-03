package com.example.onik.model

interface Repository {
    fun getMovieDetailsFromServer(id: Int): Movie
    fun getMovieDetailsFromLocalStorage(id: Int): Movie

    fun getPopularMoviesFromServer(): Array<Movie>
    fun getPopularMoviesFromLocalStorage(): Array<Movie>
}