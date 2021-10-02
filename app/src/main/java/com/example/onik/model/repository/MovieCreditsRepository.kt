package com.example.onik.model.repository

import com.example.onik.model.data.ListMovieCreditsDTO
import com.example.onik.model.data.ListMoviesDTO
import retrofit2.Callback


interface MovieCreditsRepository {
    fun getMovieCreditsFromServer(peopleId: Int, callback: Callback<ListMovieCreditsDTO>)
}
