package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchResultAPI {

    @GET("3/search/movie")
    suspend fun getFind(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): ListMoviesDTO

}
