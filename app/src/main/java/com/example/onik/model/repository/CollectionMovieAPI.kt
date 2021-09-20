package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CollectionMovieAPI {

    @GET("3/movie/{collection}")
    fun getDetails(
        @Path("collection") collection: String,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
    ): Call<ListMoviesDTO>

}