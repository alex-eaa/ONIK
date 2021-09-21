package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import retrofit2.Callback

class CollectionRepositoryImpl(private val remoteDataSourceCollections: RemoteDataSourceCollections) :
    CollectionRepository {

    override fun getCollectionFromServer(
        collection: CollectionId,
        callback: Callback<ListMoviesDTO>,
    ) {
        remoteDataSourceCollections.getCollection(collection, callback)
    }
}