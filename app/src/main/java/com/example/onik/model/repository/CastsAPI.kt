package com.example.onik.model.repository

import com.example.onik.model.data.ListCastsDTO
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CastsAPI {

    @GET("3/movie/{movieId}/credits")
    fun getCasts(
        @Path("movieId") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
    ): Call<ListCastsDTO>



    companion object {

        fun getApiCasts(): CastsAPI = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(CastsAPI::class.java)
    }
}