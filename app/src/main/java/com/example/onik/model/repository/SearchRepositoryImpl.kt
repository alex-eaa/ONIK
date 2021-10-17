package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import retrofit2.Callback

class SearchRepositoryImpl(private val remoteDataSourceSearch: RemoteDataSourceSearch) :
    SearchRepository {

    override suspend fun getSearchResultFromServer(
        searchQuery: String,
        page: Int
    ) {
        remoteDataSourceSearch.getFind(searchQuery, page)
    }

}