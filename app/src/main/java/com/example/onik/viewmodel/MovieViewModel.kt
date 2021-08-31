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

    fun getMovieFromLocalSource(id: Int) = getDataFromLocalSource(id)
    fun getMovieFromRemoteSource(id: Int) = getDataFromLocalSource(id)


    private fun getDataFromLocalSource(id: Int) {
        liveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(1000)
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.SuccessMovie(repositoryImpl.getMovieFromServer(id)))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }
}