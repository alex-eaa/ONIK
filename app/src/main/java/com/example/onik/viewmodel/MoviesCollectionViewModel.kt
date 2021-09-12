package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception
import java.lang.Math.random
import kotlin.random.Random

class MoviesCollectionViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private var moviesListLiveDataObserver: MutableMap<String, MutableLiveData<AppState>> =
        mutableMapOf("0" to MutableLiveData<AppState>())

    fun getMoviesListLiveData(key: String): LiveData<AppState>? {
        moviesListLiveDataObserver[key] = MutableLiveData<AppState>()
        return moviesListLiveDataObserver[key]
    }


    fun getDataFromLocalSource(key: String) = getData(key)
    fun getDataFromRemoteSource(key: String) = getData(key)


    private fun getData(key: String) {
        moviesListLiveDataObserver[key]?.value = AppState.LoadingMovies(key)

        Thread {
            Thread.sleep((500..1500).random().toLong())
//            if (Random.nextBoolean()) {
            if (true) {
                moviesListLiveDataObserver[key]?.postValue(AppState.SuccessMovies(
                    repositoryImpl.getListMoviesFromRemoteSource(), key))
            } else {
                moviesListLiveDataObserver[key]?.postValue(AppState.ErrorMovies(Exception("Нет связи"),
                    key))
            }
        }.start()
    }
}