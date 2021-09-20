package com.example.onik.model.data


data class ListMovies(
    val page: String? = null,
    val results: List<Movie>? = null,
    val total_pages: Int? = null,
    val total_results: Int? = null,
)