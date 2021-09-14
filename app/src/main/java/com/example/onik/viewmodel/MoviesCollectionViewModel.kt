package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl

class MoviesCollectionViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private var moviesListLiveDataObserver: MutableMap<CollectionId, MutableLiveData<AppState>> =
        mutableMapOf(CollectionId.EMPTY to MutableLiveData<AppState>())

    fun getMoviesListLiveData(id: CollectionId): LiveData<AppState>? = moviesListLiveDataObserver.run {
        remove(CollectionId.EMPTY)
        put(id, MutableLiveData<AppState>())
        get(id)
    }

    fun getDataFromLocalSource(collectionId: String) {}

    fun getDataFromRemoteSource(id: CollectionId) {
        moviesListLiveDataObserver[id]?.let {
            repositoryImpl.getListMoviesFromServer(id, it)
        }

    }

}