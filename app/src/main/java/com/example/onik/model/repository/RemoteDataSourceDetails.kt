package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.App.Companion.getSettings
import com.example.onik.model.data.MovieDTO
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceDetails {

    private val detailsMovieAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(DetailsMovieAPI::class.java)

    fun getMovieDetails(movieId: Int, callback: Callback<MovieDTO>) {
        val language = getSettings().getString("listPref_languages", "ru-RU").toString()
        detailsMovieAPI.getDetails(movieId,
            language, BuildConfig.THEMOVIEDB_API_KEY).enqueue(callback)
    }
}

