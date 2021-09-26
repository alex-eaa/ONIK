package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.onik.app.App
import com.example.onik.model.localRepository.LocalRepository
import com.example.onik.model.localRepository.LocalRepositoryImpl
import com.example.onik.model.room.MovieEntity

private const val TAG = "ViewModel"

class MoviesFavoritesViewModel : ViewModel() {

    private val localRepository: LocalRepository = LocalRepositoryImpl(App.getMovieDao())

    fun getAllMovieLocalLiveData(): LiveData<List<MovieEntity>> {
        return localRepository.getAllMovieLiveData()
    }

}