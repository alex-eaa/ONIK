package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.model.repository.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MoviesCollectionViewModel : ViewModel() {

    private val collectionRepositoryImpl: CollectionRepository = CollectionRepositoryImpl(
        RemoteDataSourceCollections())

    private val collRepositoryImpl: CollRepository = CollRepositoryImpl()

    private var moviesListLiveDataObserver: MutableMap<CollectionId, MutableLiveData<AppState>> =
        mutableMapOf()

    fun getMoviesListLiveData(id: CollectionId): LiveData<AppState>? =
        moviesListLiveDataObserver.run {
            put(id, MutableLiveData<AppState>())
            get(id)
        }


    fun getDataFromRemoteSource(collectionId: CollectionId) {
        moviesListLiveDataObserver[collectionId]?.postValue(AppState.Loading)
        collectionRepositoryImpl.getCollectionFromServer(collectionId, getCallBack(collectionId))
    }

    fun getDataFromRemoteSource2(collectionId: CollectionId) {
        moviesListLiveDataObserver[collectionId]?.postValue(AppState.Loading)
        collRepositoryImpl.getCollFromServer(collectionId, getCallBack(collectionId), 1)
    }


    private fun getCallBack(collectionId: CollectionId): Callback<ListMoviesDTO> {
        return object : Callback<ListMoviesDTO> {

            override fun onResponse(
                call: Call<ListMoviesDTO>,
                response: Response<ListMoviesDTO>,
            ) {
                val serverResponse: ListMoviesDTO? = response.body()

                moviesListLiveDataObserver[collectionId]?.postValue(
                    if (response.isSuccessful && serverResponse != null) {
                        AppState.SuccessMovies(convertListMoviesDtoToListMovies(serverResponse))
                    } else {
                        AppState.Error(Throwable(SERVER_ERROR))
                    }
                )
            }

            override fun onFailure(call: Call<ListMoviesDTO>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                moviesListLiveDataObserver[collectionId]?.postValue(AppState.Error(Throwable(t.message
                    ?: REQUEST_ERROR)))
            }
        }
    }
}