package com.example.onik.model.repository

import com.example.onik.App
import com.example.onik.BuildConfig
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.repository.CollAPI.Companion.getCollRetrofit
import com.example.onik.viewmodel.CollectionId
import io.reactivex.Single

class CollRepositoryImpl : CollRepository {
    val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()

    override fun getCollFromServer(
        collection: CollectionId,
        page: Int,
    ): Single<ListMoviesDTO> {
        return getCollRetrofit().getColl(collection.id, language, BuildConfig.THEMOVIEDB_API_KEY, page)
    }
}