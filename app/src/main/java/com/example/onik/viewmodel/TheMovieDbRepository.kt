package com.example.onik.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.onik.model.data.Movie
import com.example.onik.model.repository.RemoteDataSourceCollections
import kotlinx.coroutines.flow.Flow


class TheMovieDbRepository(
    private val service: RemoteDataSourceCollections
) {

    fun getResultStream(collectionId: CollectionId): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PAGE_SIZE / 2,
                initialLoadSize = NETWORK_PAGE_SIZE,
                maxSize = 80,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CollectionPagingSource(service, collectionId) }
        ).flow
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 20

        fun TheMovieDbRepositoryFactory(
            service: RemoteDataSourceCollections,
        ): TheMovieDbRepository {
            return TheMovieDbRepository(service)
        }
    }
}