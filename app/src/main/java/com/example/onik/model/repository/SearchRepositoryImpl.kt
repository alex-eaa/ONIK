package com.example.onik.model.repository

class SearchRepositoryImpl(private val remoteDataSourceSearch: RemoteDataSourceSearch) :
    SearchRepository {

    override suspend fun getSearchResultFromServer(
        searchQuery: String,
        page: Int
    ) {
        remoteDataSourceSearch.getFind(searchQuery, page)
    }

}