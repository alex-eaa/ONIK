package com.example.onik.model.room

import androidx.room.*

@Dao
interface MovieDao {

        @Query("SELECT * FROM MovieEntity")
        fun all(): List<MovieEntity>

        @Query("SELECT * FROM MovieEntity WHERE idMovie LIKE :idMovie")
        fun getDataByWord(idMovie: Int): List<MovieEntity>

        @Query("SELECT note FROM MovieEntity WHERE idMovie LIKE :idMovie")
        fun getNote(idMovie: Int): String

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(entity: MovieEntity)


        @Update
        fun update(entity: MovieEntity)

        @Delete
        fun delete(entity: MovieEntity)
}