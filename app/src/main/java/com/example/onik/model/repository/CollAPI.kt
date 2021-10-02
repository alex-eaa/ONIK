package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CollAPI {

    @GET("3/movie/{collection}")
    fun getColl(
        @Path("collection") collection: String,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): Call<ListMoviesDTO>


    companion object {

        fun getApiColl() = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(CollAPI::class.java)
    }
}