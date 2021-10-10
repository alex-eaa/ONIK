package com.example.onik.model.repository

import com.example.onik.model.data.ListMovieCreditsDTO
import retrofit2.Callback


interface MovieCreditsRepository {
    fun getMovieCreditsFromServer(peopleId: Int, callback: Callback<ListMovieCreditsDTO>)
}
