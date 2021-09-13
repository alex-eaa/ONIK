package com.example.onik.model

import androidx.lifecycle.MutableLiveData
import com.example.onik.viewmodel.AppState

interface Repository {
    fun getMovieDetailsFromServer(id: Int, liveData: MutableLiveData<AppState>)
    fun getListMoviesFromServer(collectionId: String, liveData: MutableLiveData<AppState>)

    fun getMovieDetailsFromLocalStorage(id: Int): Movie
    fun getListMoviesFromLocalSource(): List<Movie>
}