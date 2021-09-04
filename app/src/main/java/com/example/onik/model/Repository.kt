package com.example.onik.model

interface Repository {
    fun getMovieDetailsFromLocalStorage(id: Int): Movie
    fun getMovieDetailsFromServer(id: Int): Movie

    fun getListMoviesFromRemoteSource(): Array<Movie>
    fun getListMoviesFromLocalSource(): Array<Movie>
}