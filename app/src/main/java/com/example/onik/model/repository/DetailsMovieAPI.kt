package com.example.onik.model.repository

import android.text.style.BulletSpan
import com.example.onik.model.data.MovieDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DetailsMovieAPI {

    @GET("3/movie/{movieId}")
    fun getDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
    ): Call<MovieDTO>

}
