package com.example.onik.model.repository

import com.example.onik.BuildConfig
import com.example.onik.app.App
import com.example.onik.model.data.CastDTO
import com.example.onik.model.data.ListCastsDTO
import com.example.onik.model.data.ListMovieCreditsDTO
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.repository.CastAPI.Companion.getApiCast
import com.example.onik.model.repository.CastsAPI.Companion.getApiCasts
import com.example.onik.model.repository.MovieCreditsAPI.Companion.getApiMovieCredits
import retrofit2.Callback


class MovieCreditsRepositoryImpl : MovieCreditsRepository {

    override fun getMovieCreditsFromServer(peopleId: Int, callback: Callback<ListMovieCreditsDTO>) {
        val language = App.getSettings().getString("listPref_languages", "ru-RU").toString()
        getApiMovieCredits().getMovieCredits(peopleId, language, BuildConfig.THEMOVIEDB_API_KEY).enqueue(callback)
    }


}
