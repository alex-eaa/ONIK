package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val moviesListLiveData1: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val moviesListLiveData2: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val moviesListLiveData3: MutableLiveData<AppState> = MutableLiveData<AppState>()

    fun getMoviesListLiveData1() = moviesListLiveData1
    fun getMoviesListLiveData2() = moviesListLiveData2
    fun getMoviesListLiveData3() = moviesListLiveData3

    fun getDataFromLocalSource() = getData()
    fun getDataFromRemoteSource() = getData()

    private fun getData() {
        moviesListLiveData1.value = AppState.Loading

        Thread {
            Thread.sleep(1000)
            if (true) {
                moviesListLiveData1.postValue(AppState.SuccessMovies1(repositoryImpl.getListMoviesFromRemoteSource()))
                moviesListLiveData2.postValue(AppState.SuccessMovies2(repositoryImpl.getListMoviesFromRemoteSource()))
                moviesListLiveData3.postValue(AppState.SuccessMovies3(repositoryImpl.getListMoviesFromRemoteSource()))
            } else {
                moviesListLiveData1.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }

}