package com.example.onik.model

import com.example.onik.viewmodel.AppState

interface Repository {
    fun getMovieDetailsFromServer(id: Int): AppState
    fun getListMoviesFromServer(collectionId: String): AppState

    fun getMovieDetailsFromLocalStorage(id: Int): Movie
    fun getListMoviesFromLocalSource(): List<Movie>
}