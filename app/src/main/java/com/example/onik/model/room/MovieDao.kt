package com.example.onik.model.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieEntity WHERE favorite = 'true' ORDER BY title")
    fun allFavoritesByTitle(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE favorite = 'true' ORDER BY title DESC")
    fun allFavoritesByTitleDesc(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE favorite = 'true' ORDER BY vote_average ")
    fun allFavoritesByVote(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE favorite = 'true' ORDER BY vote_average DESC")
    fun allFavoritesByVoteDesc(): LiveData<List<MovieEntity>>

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