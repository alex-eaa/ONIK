package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class MoviesListViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val moviesListLiveData: MutableLiveData<AppState> = MutableLiveData<AppState>()

    fun getMoviesListLiveData() = moviesListLiveData

    fun getDataFromLocalSource(id: Int) = getData()
    fun getDataFromRemoteSource(id: Int) = getData()


    private fun getData() {
        moviesListLiveData.value = AppState.Loading

        Thread {
            Thread.sleep(700)
            if (true) {
                moviesListLiveData.postValue(AppState.SuccessMovies(repositoryImpl.getListMoviesFromRemoteSource()))
            } else {
                moviesListLiveData.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }
}