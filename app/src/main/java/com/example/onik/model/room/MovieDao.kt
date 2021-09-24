package com.example.onik.model.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable

@Dao
interface MovieDao {

        @Query("SELECT * FROM MovieEntity")
        fun all(): List<MovieEntity>

        @Query("SELECT * FROM MovieEntity WHERE idMovie LIKE :idMovie")
        fun getDataByWord(idMovie: Int): List<MovieEntity>

        @Query("SELECT * FROM MovieEntity WHERE idMovie = :idMovie")
        fun getMovieLiveData(idMovie: Int): LiveData<MovieEntity>

        @Query("SELECT * FROM MovieEntity WHERE idMovie = :idMovie")
        fun getMovieRx(idMovie: Int): Flowable<MovieEntity>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(entity: MovieEntity)

        @Update
        fun update(entity: MovieEntity)

        @Delete
        fun delete(entity: MovieEntity)
}