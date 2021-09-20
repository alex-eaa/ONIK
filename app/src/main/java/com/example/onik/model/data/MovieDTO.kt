package com.example.onik.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDTO (
    val id: Int?,
    val adult: Boolean?,
    val backdrop_path: String?,
    val budget: Int?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val title: String?,
    val vote_count: Int?,
    val vote_average: Double?,
    val release_date: String?,
    val runtime: Int?,
    val revenue: Int?,
    val genres: List<GenresDTO>?,
) : Parcelable {

    @Parcelize
    data class GenresDTO(
        val id: Int?,
        val name: String?,
    ) : Parcelable
}