package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.app.App
import com.example.onik.model.data.ListMoviesDTO
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceSearch {

    private val collectionAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(SearchResultAPI::class.java)

    fun getFind(searchQuery: String, callback: Callback<ListMoviesDTO>) {
        val adult: Boolean = App.getSettings().getBoolean("pref_adult", true)
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()

        collectionAPI.getFind(searchQuery,
            language,
            adult,
            BuildConfig.THEMOVIEDB_API_KEY)
            .enqueue(callback)
    }
}
