package com.example.onik.model.repository

import android.util.Log
import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.viewmodel.CollectionId
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Callback
import retrofit2.Response

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