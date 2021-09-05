package com.example.onik.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class MovieViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val movieDetailsLiveData: MutableLiveData<AppState> = MutableLiveData<AppState>()

    fun getMovieDetailsLiveData()  = movieDetailsLiveData

    fun getDataFromLocalSource(id: Int) = getData(id)
    fun getDataFromRemoteSource(id: Int) = getData(id)


    private fun getData(id: Int) {
        movieDetailsLiveData.value = AppState.Loading

        Thread {
            Thread.sleep(500)
            if (true) {
                movieDetailsLiveData.postValue(AppState.SuccessMovie(repositoryImpl.getMovieDetailsFromServer(id)))
            } else {
                movieDetailsLiveData.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }

}