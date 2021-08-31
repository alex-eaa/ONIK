package com.example.onik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception
import kotlin.random.Random

class MovieViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    private val repositoryImpl: Repository = RepositoryImpl()

    fun getLiveData() = liveDataToObserve

    fun getMovieFromLocalSource() = getDataFromLocalSource()
    fun getMovieFromRemoteSource() = getDataFromLocalSource()


    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(1000)
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.SuccessMovie(repositoryImpl.getMovieFromServer()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }
}