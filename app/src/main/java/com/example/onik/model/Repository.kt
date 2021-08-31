package com.example.onik.model

interface Repository {
    fun getMovieFromServer(id: Int): Movie
    fun getMovieFromLocalStorage(id: Int): Movie

    fun getPopularMoviesFromServer(): Array<Movie>
    fun getPopularMoviesFromLocalStorage(): Array<Movie>
}