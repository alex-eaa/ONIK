package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.app.App
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceCollections {

    private val collectionAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(CollectionMovieAPI::class.java)

    fun getCollection(collection: CollectionId, callback: Callback<ListMoviesDTO>) {
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()
        collectionAPI.getCollection(collection.id, language, BuildConfig.THEMOVIEDB_API_KEY).enqueue(callback)
    }
}
