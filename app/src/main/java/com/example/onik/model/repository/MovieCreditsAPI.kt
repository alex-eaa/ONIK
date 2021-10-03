package com.example.onik.model.repository

import com.example.onik.model.data.CastDTO
import com.example.onik.model.data.ListMovieCreditsDTO
import com.example.onik.model.data.ListMoviesDTO
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieCreditsAPI {

    @GET("3/person/{peopleId}/movie_credits")
    fun getMovieCredits(
        @Path("peopleId") peopleId: Int,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
    ): Call<ListMovieCreditsDTO>



    companion object {

        fun getApiMovieCredits(): MovieCreditsAPI = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(MovieCreditsAPI::class.java)
    }
}