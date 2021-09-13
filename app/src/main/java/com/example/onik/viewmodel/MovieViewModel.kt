package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl

class MovieViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()

    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver


    fun getDataFromLocalSource(id: Int) {}

    fun getDataFromRemoteSource(id: Int) {
        repositoryImpl.getMovieDetailsFromServer(id, movieDetailsLiveDataObserver)
    }

}