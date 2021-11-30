package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onik.model.repository.CollectionRepository
import com.example.onik.model.repository.CollectionRepositoryImpl
import com.example.onik.model.repository.RemoteDataSourceCollections
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val collectionRepositoryImpl: CollectionRepository = CollectionRepositoryImpl(
        RemoteDataSourceCollections()
    )

    private var moviesListLiveDataObserver: Map<CollectionId, MutableLiveData<AppState>> =
        mapOf(
            CollectionId.POPULAR to MutableLiveData<AppState>(),
            CollectionId.NOW_PLAYING to MutableLiveData<AppState>(),
            CollectionId.TOP_RATED to MutableLiveData<AppState>(),
            CollectionId.UPCOMING to MutableLiveData<AppState>()
        )

    fun getMoviesListLiveData(collectionId: CollectionId): LiveData<AppState>? =
        moviesListLiveDataObserver[collectionId]


    fun getOneCollectionCoroutines(collectionId: CollectionId, page: Int) {
        viewModelScope.launch {
            collectionRepositoryImpl.getCollectionFromServer(collectionId, page)
                .onStart { moviesListLiveDataObserver[collectionId]?.value = AppState.Loading }
                .catch { exception ->
                    moviesListLiveDataObserver[collectionId]?.value = AppState.Error(exception)
                }
                .collect { listMovies ->
                    moviesListLiveDataObserver[collectionId]?.value = listMovies.results?.let {
                        AppState.SuccessMovies(it)
                    }
                }
        }
    }

}