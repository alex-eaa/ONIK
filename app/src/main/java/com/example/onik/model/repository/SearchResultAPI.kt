package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.MovieDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface SearchResultAPI {

    @GET("3/search/movie")
    fun getFind(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("api_key") api_key: String,
    ): Call<ListMoviesDTO>

}
