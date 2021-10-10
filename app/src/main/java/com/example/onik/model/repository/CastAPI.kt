package com.example.onik.model.repository

import com.example.onik.model.data.CastDTO
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CastAPI {

    @GET("3/person/{peopleId}")
    fun getCast(
        @Path("peopleId") peopleId: Int,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
    ): Call<CastDTO>



    companion object {

        fun getApiCast(): CastAPI = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(CastAPI::class.java)
    }
}