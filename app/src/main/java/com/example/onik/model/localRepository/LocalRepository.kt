package com.example.onik.model.localRepository

import androidx.lifecycle.LiveData
import com.example.onik.model.data.MovieLocal
import com.example.onik.model.room.MovieEntity
import io.reactivex.Flowable

interface LocalRepository {
    fun getMovieRx(movieId: Int): Flowable<MovieEntity>
    fun getMovieLiveData(movieId: Int): LiveData<MovieEntity>
    fun saveMovie(movieEntity: MovieEntity)
}