package com.example.onik.model.repository

interface SearchRepository {
    suspend fun getSearchResultFromServer(searchQuery: String, page: Int)
}
