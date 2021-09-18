package com.example.onik.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.*

class MovieViewModel : ViewModel() {

    private val repositoryImpl: Repository = RepositoryImpl()

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()

    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver


    fun getDataFromLocalSource(id: Int) {}

    fun getDataFromRemoteSource(id: Int) {
        repositoryImpl.getMovieDetailsFromServer(id, movieDetailsLiveDataObserver)
    }

    fun startServiceDetailsLoader (context: Context?, id: Int){
        context.let {
            it?.startService(Intent(it, ServiceDetailsLoader::class.java).apply {
                putExtra(MOVIE_ID_EXTRA, id)
            })
        }
    }

}