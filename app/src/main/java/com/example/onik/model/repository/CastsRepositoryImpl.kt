package com.example.onik.model.repository

import com.example.onik.App
import com.example.onik.BuildConfig
import com.example.onik.model.data.ListCastsDTO
import com.example.onik.model.repository.CastsAPI.Companion.getApiCasts
import retrofit2.Callback


class CastsRepositoryImpl : CastsRepository {

    override fun getListCastsFromServer(
        movieId: Int,
        callback: Callback<ListCastsDTO>,
    ) {
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()
        getApiCasts().getCasts(movieId, language, BuildConfig.THEMOVIEDB_API_KEY).enqueue(callback)
    }
}
