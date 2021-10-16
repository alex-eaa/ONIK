package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.Movie
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.model.repository.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MoviesCollectionViewModel : ViewModel() {

    private val collectionRepositoryImpl: CollectionRepository = CollectionRepositoryImpl(
        RemoteDataSourceCollections()
    )

    private var moviesListLiveDataObserver: MutableMap<CollectionId, MutableLiveData<AppState>> =
        mutableMapOf()

    fun getMoviesListLiveData(collectionId: CollectionId): LiveData<AppState>? =
        moviesListLiveDataObserver.run {
            put(collectionId, MutableLiveData<AppState>())
            get(collectionId)
        }


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