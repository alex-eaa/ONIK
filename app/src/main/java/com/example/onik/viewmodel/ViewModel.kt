package com.example.onik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class ViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val moviesListLiveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val moviesListLiveData1: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val moviesListLiveData2: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val moviesListLiveData3: MutableLiveData<AppState> = MutableLiveData<AppState>()
    private val movieLiveData: MutableLiveData<AppState> = MutableLiveData<AppState>()

    fun getMoviesListLiveData() = moviesListLiveData
    fun getMoviesListLiveData1() = moviesListLiveData1
    fun getMoviesListLiveData2() = moviesListLiveData2
    fun getMoviesListLiveData3() = moviesListLiveData3
    fun getMovieLiveData() = movieLiveData


    fun getListMoviesFromLocalSource(id: Int) = getListMoviesData()
    fun getListMoviesFromRemoteSource(id: Int) = getListMoviesData()

    fun getAllListMoviesFromLocalSource() = getAllListMoviesData()
    fun getAllListMoviesFromRemoteSource() = getAllListMoviesData()

    fun getMovieFromLocalSource(id: Int) = getMovieData(id)
    fun getMovieFromRemoteSource(id: Int) = getMovieData(id)



    private fun getAllListMoviesData() {
        moviesListLiveData1.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            if (true) {
                moviesListLiveData1.postValue(AppState.SuccessMovies1(repositoryImpl.getListMoviesFromRemoteSource()))
                moviesListLiveData2.postValue(AppState.SuccessMovies2(repositoryImpl.getListMoviesFromRemoteSource()))
                moviesListLiveData3.postValue(AppState.SuccessMovies3(repositoryImpl.getListMoviesFromRemoteSource()))
            } else {
                moviesListLiveData1.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }


    private fun getMovieData(id: Int) {
        movieLiveData.value = AppState.Loading

        Thread {
            Thread.sleep(1000)
            if (true) {
                movieLiveData.postValue(AppState.SuccessMovie(repositoryImpl.getMovieDetailsFromServer(id)))
            } else {
                movieLiveData.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }


    private fun getListMoviesData() {
        moviesListLiveData.value = AppState.Loading

        Thread {
            Thread.sleep(2000)
            if (true) {
                moviesListLiveData.postValue(AppState.SuccessMovies(repositoryImpl.getListMoviesFromRemoteSource()))
            } else {
                moviesListLiveData.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }
}