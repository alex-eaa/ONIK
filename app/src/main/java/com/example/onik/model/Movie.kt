package com.example.onik.model

data class Movie(
    val poster_path: String = "",
    val adult: Boolean = false,
    val overview: String = "",
    val release_date: String = "",
    val id: Int = 0,
    val original_title: String = "",
    val original_language: String = "",
    val title: String = "",
    val backdrop_path: String = "",
    val popularity: Double = 0.0,
    val vote_count: Int = 0,
    val vote_average: Double = 0.0,
    val runtime: Int = 0,
    val budget: Int = 0,
    val revenue: Int = 0,
    val genre_ids: List<Genre> = getDefaultGenres(),
)

fun getDefaultGenres() = listOf(
    Genre(18, "Drama"),
    Genre(28, "Action"),
    Genre(44, "Fantasy")
)