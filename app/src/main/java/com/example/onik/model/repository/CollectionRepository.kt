package com.example.onik.model.repository

import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import retrofit2.Callback
import retrofit2.Response

interface CollectionRepository {
    fun getCollectionFromServer(collectionId: CollectionId, page: Int): Flow<ListMovies>
}
