package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import retrofit2.Callback

interface SearchRepository {
    fun getSearchResultFromServer(searchQuery: String, callback: Callback<ListMoviesDTO>)
}
