package com.example.onik.model.repository

import android.util.Log
import com.example.onik.BuildConfig
import com.example.onik.App
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import retrofit2.Callback
import retrofit2.Response
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

    suspend fun getCollection(collection: CollectionId, page: Int) : ListMoviesDTO {
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()
        val data = collectionAPI.getCollection(collection.id, language, BuildConfig.THEMOVIEDB_API_KEY, page)

        Log.d("PagingRemoteDataSource", "getCollection page = $page")
        Log.d("PagingRemoteDataSource", "received collection = $data")
        return data
    }
}
