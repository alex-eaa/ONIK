package com.example.onik.model.data

data class MovieLocal(
    var idMovie: Int = 0,
    var note: String = "",
    var favorite: Boolean = false,
    var title: String = "",
    var poster_path: String = "",
    var vote_average: Double = 0.0,
    var release_date: String = "0000-00-00",
)
