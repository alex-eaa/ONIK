package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CollectionMovieAPI {

    @GET("3/movie/{collection}")
    suspend fun getCollection(
        @Path("collection") collection: String,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): ListMoviesDTO

}