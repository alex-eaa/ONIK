package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl

class MoviesCollectionViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private var moviesListLiveDataObserver: MutableMap<String, MutableLiveData<AppState>> =
        mutableMapOf("0" to MutableLiveData<AppState>())

    fun getMoviesListLiveData(key: String): LiveData<AppState>? = moviesListLiveDataObserver.run {
        remove("0")
        put(key, MutableLiveData<AppState>())
        get(key)
    }

    fun getDataFromLocalSource(collectionId: String) {}

    fun getDataFromRemoteSource(collectionId: String) {
        moviesListLiveDataObserver[collectionId]?.let {
            repositoryImpl.getListMoviesFromServer(collectionId, it)
        }

    }

}