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


    fun getDataFromLocalSource(key: String) = getData(key)
    fun getDataFromRemoteSource(key: String) = getData(key)


    private fun getData(key: String) {
        moviesListLiveDataObserver[key]?.value = AppState.LoadingMovies(key)

        Thread {
            moviesListLiveDataObserver[key]?.postValue(repositoryImpl.getListMoviesFromServer(key))
        }.start()
    }
}