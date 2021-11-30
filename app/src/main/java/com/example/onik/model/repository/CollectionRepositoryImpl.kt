package com.example.onik.model.repository

import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.viewmodel.CollectionId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CollectionRepositoryImpl(private val remoteDataSourceCollections: RemoteDataSourceCollections) :
    CollectionRepository {

    override fun getCollectionFromServer(
        collectionId: CollectionId,
        page: Int
    ) : Flow<ListMovies> {
        return flow{
            val data = remoteDataSourceCollections.getCollection(collectionId, page)
                emit(convertListMoviesDtoToListMovies(data))
        }.flowOn(Dispatchers.IO)
    }
}