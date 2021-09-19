package com.example.onik.model

import android.os.Parcelable

data class Movie(
    var poster_path: String? = null,
    var adult: Boolean? = null,
    var overview: String? = null,
    var release_date: String? = null,
    var id: Int? = null,
    var title: String? = null,
    var backdrop_path: String? = null,
    var popularity: Double? = null,
    var vote_count: Int? = null,
    var vote_average: Double? = null,
    var runtime: Int? = null,
    var budget: Int? = null,
    var revenue: Int? = null,
    var genres: List<Genre>? = null
) {

    data class Genre(
        val id: Int? = null,
        val name: String? = null,
    )
}

fun getDefaultGenres() = listOf(
    Movie.Genre(0, ""),
)