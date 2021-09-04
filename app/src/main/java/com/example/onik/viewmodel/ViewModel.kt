package com.example.onik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class ViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val moviesListLiveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    fun getPopularMoviesLiveData() = moviesListLiveData

    private val movieLiveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    fun getMovieLiveData() = movieLiveData

    fun getListMoviesFromLocalSource() = getPopularMoviesData()
    fun getListMoviesFromRemoteSource() = getPopularMoviesData()

    fun getMovieFromLocalSource(id: Int) = getMovieData(id)
    fun getMovieFromRemoteSource(id: Int) = getMovieData(id)


    private fun getPopularMoviesData() {
        moviesListLiveData.value = AppState.Loading

        Thread {
            Thread.sleep(300)
            if (true) {
                moviesListLiveData.postValue(AppState.SuccessMovies(repositoryImpl.getListMoviesFromRemoteSource()))
            } else {
                moviesListLiveData.postValue(AppState.Error(Exception("Нет связи")))
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
}