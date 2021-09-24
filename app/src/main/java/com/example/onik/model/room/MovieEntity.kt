package com.example.onik.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey
    var idMovie: Int = 0,
    var note: String = "",
    var favorite: String = "false",
    var title: String = "",
)
