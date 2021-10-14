package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.model.repository.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MoviesCollectionViewModel : ViewModel() {
    init {
        Log.d("thread", "init MoviesCollectionViewModel: ${this.toString()}")
    }

    private var compositeDisposable = CompositeDisposable()

//    private val collectionRepositoryImpl: CollectionRepository = CollectionRepositoryImpl(
//        RemoteDataSourceCollections()
//    )

    private val collRepositoryImpl: CollRepository = CollRepositoryImpl()

    var listCollectionId: MutableList<CollectionId> = mutableListOf()

    private var moviesListLiveDataObserver: MutableMap<CollectionId, MutableLiveData<AppState>> =
        mutableMapOf()

    fun getMoviesListLiveData(id: CollectionId): LiveData<AppState>? =
        moviesListLiveDataObserver.run {
            put(id, MutableLiveData<AppState>())
            get(id)
        }

//    fun getDataFromRemoteSource2(collectionId: CollectionId) {
//        moviesListLiveDataObserver[collectionId]?.postValue(AppState.Loading)
//        collectionRepositoryImpl.getCollectionFromServer(collectionId, getCallBack(collectionId))
//    }


    fun getAllCollections() {
        Log.d("thread", "getDataFromRemoteSource: ${Thread.currentThread().name}")
        compositeDisposable.add(Observable.fromIterable(listCollectionId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                getOneCollection(it, 1)
                Log.d("thread", "${it.id} currentThread: ${Thread.currentThread().name}")
            }
        )
    }


    fun getOneCollection(collectionId: CollectionId, page: Int) {
        moviesListLiveDataObserver[collectionId]?.postValue(AppState.Loading)

        compositeDisposable.add(
            collRepositoryImpl.getCollFromServer(collectionId, page)
                .flatMap { Single.just(convertListMoviesDtoToListMovies(it)) }
                .subscribeOn(Schedulers.io())
                .subscribe({ listMovies ->
                    Log.d("thread", "subscribe listMovies: ${Thread.currentThread().name}")
                    listMovies.results?.let {
                        moviesListLiveDataObserver[collectionId]?.postValue(
                            AppState.SuccessMovies(it)
                        )
                    }
                }, {
                    moviesListLiveDataObserver[collectionId]?.postValue(AppState.Error(it))
                }
                )
        )
    }


//    private fun getCallBack(collectionId: CollectionId): Callback<ListMoviesDTO> {
//        return object : Callback<ListMoviesDTO> {
//
//            override fun onResponse(
//                call: Call<ListMoviesDTO>,
//                response: Response<ListMoviesDTO>,
//            ) {
//                val serverResponse: ListMoviesDTO? = response.body()
//
//                moviesListLiveDataObserver[collectionId]?.postValue(
//                    if (response.isSuccessful && serverResponse != null) {
//                        AppState.SuccessMovies(convertListMoviesDtoToListMovies(serverResponse))
//                    } else {
//                        AppState.Error(Throwable(SERVER_ERROR))
//                    }
//                )
//            }
//
//            override fun onFailure(call: Call<ListMoviesDTO>, t: Throwable) {
//                Log.d(TAG, t.message.toString())
//                moviesListLiveDataObserver[collectionId]?.postValue(
//                    AppState.Error(
//                        Throwable(
//                            t.message
//                                ?: REQUEST_ERROR
//                        )
//                    )
//                )
//            }
//        }
//    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}