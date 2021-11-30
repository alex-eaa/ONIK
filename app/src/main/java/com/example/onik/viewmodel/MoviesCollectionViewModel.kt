package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.onik.model.data.Movie
import com.example.onik.model.repository.RemoteDataSourceCollections


private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MoviesCollectionViewModel(
    private val collectionId: CollectionId
) : ViewModel() {


    val moviesLiveData: LiveData<PagingData<Movie>> =
        getCollectionStreamLiveData()


    private fun getCollectionStreamLiveData(): LiveData<PagingData<Movie>> {
        val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE / 4,
            initialLoadSize = PAGE_SIZE,
            maxSize = 80,
            enablePlaceholders = false
        )

        val pager: Pager<Int, Movie> = Pager(
            config = pagingConfig,
            pagingSourceFactory = { CollectionPagingSource(RemoteDataSourceCollections(), collectionId) }
        )
        return pager.liveData.cachedIn(viewModelScope)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}