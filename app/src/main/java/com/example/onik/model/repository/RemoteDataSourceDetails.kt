package com.example.onik.model.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.onik.BuildConfig
import com.example.onik.model.data.MovieDTO
import com.google.gson.GsonBuilder

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceDetails(application: Application) {

    val sp: SharedPreferences = getDefaultSharedPreferences(application.applicationContext)

    private val detailsMovieAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(DetailsMovieAPI::class.java)

    fun getMovieDetails(movieId: Int, callback: Callback<MovieDTO>) {
        val adult: Boolean = sp.getBoolean("pref_adult", true)
        var language = "ru-RU"
        sp.getString("listPref_languages", "ru-RU")?.let {
            language = it
        }
        Log.d("zzz", adult.toString())
        Log.d("zzz", language)


        detailsMovieAPI.getDetails(movieId, language, BuildConfig.THEMOVIEDB_API_KEY, adult).enqueue(callback)
    }
}

