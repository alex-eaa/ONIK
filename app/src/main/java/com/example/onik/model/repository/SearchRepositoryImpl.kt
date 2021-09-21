package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import retrofit2.Callback

class SearchRepositoryImpl(private val remoteDataSourceSearch: RemoteDataSourceSearch) :
    SearchRepository {

    override fun getSearchResultFromServer(
        searchQuery: String,
        callback: Callback<ListMoviesDTO>,
    ) {
        remoteDataSourceSearch.getCollection(searchQuery, callback)
    }

}