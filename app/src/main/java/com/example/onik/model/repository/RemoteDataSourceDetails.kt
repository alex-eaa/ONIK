package com.example.onik.model.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.onik.BuildConfig
import com.example.onik.model.data.MovieDTO
import com.google.gson.GsonBuilder
import com.example.onik.app.App.Companion.getSettings
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceDetails(application: Application) {

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

