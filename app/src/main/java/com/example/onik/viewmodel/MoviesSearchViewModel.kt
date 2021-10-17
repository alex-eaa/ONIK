package com.example.onik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.onik.model.data.Movie
import com.example.onik.model.repository.RemoteDataSourceSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class MoviesSearchViewModel : ViewModel() {

//    val moviesSearch: StateFlow<PagingData<Movie>> =
//        getSearchStreamFlow("query")
//            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun findDataOnRemoteSource(query: String): Flow<PagingData<Movie>> {
        return getSearchStreamFlow(query)
    }

    private fun getSearchStreamFlow(query: String): Flow<PagingData<Movie>> {
        val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE / 2,
            initialLoadSize = PAGE_SIZE,
            maxSize = 80,
            enablePlaceholders = false
        )

        val pager: Pager<Int, Movie> = Pager(
            config = pagingConfig,
            pagingSourceFactory = { MoviesSearchPagingSource(RemoteDataSourceSearch(), query) }
        )

        return pager.flow.cachedIn(viewModelScope)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}