package com.example.onik.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.MovieDTO
import com.example.onik.model.Repository
import com.example.onik.model.RepositoryImpl
import java.lang.Exception

class MovieViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()

    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver


    fun getDataFromLocalSource(id: Int) = getData(id)
    fun getDataFromRemoteSource(id: Int) = getData(id)


    private fun getData(id: Int) {
        movieDetailsLiveDataObserver.value = AppState.Loading

        Thread {
            movieDetailsLiveDataObserver.postValue(repositoryImpl.getMovieDetailsFromServer(id))
        }.start()
    }

}