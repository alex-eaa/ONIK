package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.model.repository.RemoteDataSourceSearch
import com.example.onik.model.repository.SearchRepository
import com.example.onik.model.repository.SearchRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MoviesSearchViewModel : ViewModel() {

    private val searchRepositoryImpl: SearchRepository = SearchRepositoryImpl(
        RemoteDataSourceSearch())

    private var moviesListLiveDataObserver: MutableLiveData<AppState> = MutableLiveData<AppState>()

    val moviesListLiveData: LiveData<AppState> = moviesListLiveDataObserver


    fun findDataOnRemoteSource(searchQuery: String) {
        moviesListLiveDataObserver.postValue(AppState.Loading)
        searchRepositoryImpl.getSearchResultFromServer(searchQuery, callBack)
    }


    private val callBack = object : Callback<ListMoviesDTO> {
        override fun onResponse(
            call: Call<ListMoviesDTO>,
            response: Response<ListMoviesDTO>,
        ) {
            val serverResponse: ListMoviesDTO? = response.body()

            moviesListLiveDataObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    AppState.SuccessMovies(convertListMoviesDtoToListMovies(serverResponse))
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<ListMoviesDTO>, t: Throwable) {
            Log.d(TAG, t.message.toString())
            moviesListLiveDataObserver.postValue(AppState.Error(Throwable(t.message
                ?: REQUEST_ERROR)))
        }
    }
}