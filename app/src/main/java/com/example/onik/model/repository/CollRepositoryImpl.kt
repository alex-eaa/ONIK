package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.app.App
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.repository.CollAPI.Companion.getApiColl
import com.example.onik.viewmodel.CollectionId
import retrofit2.Callback

class CollRepositoryImpl : CollRepository {

    override fun getCollFromServer(
        collection: CollectionId,
        callback: Callback<ListMoviesDTO>,
        page: Int,
    ) {
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()
        getApiColl().getColl(collection.id, language, BuildConfig.THEMOVIEDB_API_KEY, page)
            .enqueue(callback)
    }
}