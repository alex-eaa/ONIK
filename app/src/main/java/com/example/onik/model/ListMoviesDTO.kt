package com.example.onik.model

data class ListMoviesDTO(
    val page: String?,
    val results: List<MovieDTO>?,
    val total_pages: Int?,
    val total_results: Int?,
)
