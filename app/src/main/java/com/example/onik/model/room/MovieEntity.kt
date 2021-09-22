package com.example.onik.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey
    val idMovie: Int,
    val note: String?,
    val title: String?,
    val vote_average: Double?,
    val poster_path: String?,
)
