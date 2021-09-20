package com.example.onik.model

import androidx.lifecycle.MutableLiveData
import com.example.onik.model.data.Movie
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.CollectionId

interface Repository {
    fun getMovieDetailsFromServer(id: Int, liveData: MutableLiveData<AppState>)
    fun getListMoviesFromServer(collectionId: CollectionId, liveData: MutableLiveData<AppState>)

    fun getMovieDetailsFromLocalStorage(id: Int): Movie
    fun getListMoviesFromLocalSource(): List<Movie>
}