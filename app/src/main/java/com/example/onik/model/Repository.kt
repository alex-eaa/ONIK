package com.example.onik.model

import androidx.lifecycle.MutableLiveData
import com.example.onik.viewmodel.AppState

interface Repository {
    fun getMovieDetailsFromLocalStorage(id: Int): Movie
    fun getMovieDetailsFromServer(id: Int): AppState

    fun getListMoviesFromRemoteSource(): List<Movie>
    fun getListMoviesFromLocalSource(): List<Movie>
}