package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RemoteDataSourceSearch {

    private val collectionAPI = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(SearchResultAPI::class.java)

    fun getCollection(searchQuery: String, callback: Callback<ListMoviesDTO>) {
        collectionAPI.getCollection(searchQuery, "ru-RU", true, BuildConfig.THEMOVIEDB_API_KEY)
            .enqueue(callback)
    }
}
