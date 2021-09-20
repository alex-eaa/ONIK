package com.example.onik.model.repository

import com.example.onik.model.data.MovieDTO


interface DetailsRepository {
    fun getMovieDetailsFromServer(movieId: Int, callback: retrofit2.Callback<MovieDTO>)
}
