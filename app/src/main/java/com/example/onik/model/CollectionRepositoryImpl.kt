package com.example.onik.model

import com.example.onik.viewmodel.CollectionId

class CollectionRepositoryImpl(private val remoteDataSourceCollections: RemoteDataSourceCollections) :
    CollectionRepository {

    override fun getCollectionFromServer(
        collection: CollectionId,
        callback: retrofit2.Callback<ListMoviesDTO>,
    ) {
        remoteDataSourceCollections.getCollection(collection, callback)
    }

}