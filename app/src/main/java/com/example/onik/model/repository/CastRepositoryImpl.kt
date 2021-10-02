package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.app.App
import com.example.onik.model.data.CastDTO
import com.example.onik.model.data.ListCastsDTO
import com.example.onik.model.repository.CastAPI.Companion.getApiCast
import com.example.onik.model.repository.CastsAPI.Companion.getApiCasts
import retrofit2.Callback


class CastRepositoryImpl : CastRepository {

    override fun getCastFromServer(peopleId: Int, callback: Callback<CastDTO>) {
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()
        getApiCast().getCast(peopleId, language, BuildConfig.THEMOVIEDB_API_KEY).enqueue(callback)
    }


}