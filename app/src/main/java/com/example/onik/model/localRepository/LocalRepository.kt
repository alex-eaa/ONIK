package com.example.onik.model.localRepository

import androidx.lifecycle.LiveData
import com.example.onik.model.data.MovieLocal
import com.example.onik.model.room.MovieEntity
import io.reactivex.Flowable
import io.reactivex.Single

const val ORDER_BY_TITLE = "ORDER_BY_TITLE"
const val ORDER_BY_TITLE_DESC = "ORDER_BY_TITLE_DESC"
const val ORDER_BY_VOTE = "ORDER_BY_VOTE"
const val ORDER_BY_VOTE_DESC = "ORDER_BY_VOTE_DESC"

interface LocalRepository {
    fun getMovieRx(movieId: Int): Single<MovieEntity>

    fun getMovieLiveData(movieId: Int): LiveData<MovieEntity>
    fun getAllMovieLiveData(orderBy: String): LiveData<List<MovieEntity>>
    fun saveMovie(movieLocal: MovieLocal)
}