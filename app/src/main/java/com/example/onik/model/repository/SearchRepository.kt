package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import retrofit2.Callback

interface SearchRepository {
    suspend fun getSearchResultFromServer(searchQuery: String, page: Int)
}
