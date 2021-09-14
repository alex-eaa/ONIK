
package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class MovieViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> = MutableLiveData<AppState>()

    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver


    fun getDataFromLocalSource(id: Int) = getData(id)
    fun getDataFromRemoteSource(id: Int) = getData(id)


    private fun getData(id: Int) {
        movieDetailsLiveDataObserver.value = AppState.Loading

        Thread {
            Thread.sleep((300..600).random().toLong())
            if (true) {
                movieDetailsLiveDataObserver.postValue(AppState.SuccessMovie(repositoryImpl.getMovieDetailsFromServer(id)))
            } else {
                movieDetailsLiveDataObserver.postValue(AppState.Error(Exception("Нет связи")))
            }

        }.start()
    }

}