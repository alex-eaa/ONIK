package com.example.onik.model.localRepository

import androidx.lifecycle.LiveData
import com.example.onik.model.data.MovieLocal
import com.example.onik.model.data.convertMovieLocalToEntity
import com.example.onik.model.room.MovieDao
import com.example.onik.model.room.MovieEntity
import io.reactivex.Flowable

class LocalRepositoryImpl (private val localDataSource: MovieDao) : LocalRepository{

    override fun getMovieRx(movieId: Int): Flowable<MovieEntity> {
        return localDataSource.getMovieRx(movieId)
    }

    override fun getMovieLiveData(movieId: Int): LiveData<MovieEntity> {
        return localDataSource.getMovieLiveData(movieId)
    }

    override fun getAllMovieLiveData(): LiveData<List<MovieEntity>> {
        return localDataSource.allFavorites()
    }

    override fun saveMovie(movieLocal: MovieLocal) {
        localDataSource.insert(convertMovieLocalToEntity(movieLocal))
    }
}