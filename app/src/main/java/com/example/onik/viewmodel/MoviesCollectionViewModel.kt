package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.*
import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.Movie
import com.example.onik.model.data.MovieDTO
import com.example.onik.model.repository.CollectionRepository
import com.example.onik.model.repository.CollectionRepositoryImpl
import com.example.onik.model.repository.RemoteDataSourceCollections

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MoviesCollectionViewModel : ViewModel() {

    private val collectionRepositoryImpl: CollectionRepository = CollectionRepositoryImpl(
        RemoteDataSourceCollections())

    private var moviesListLiveDataObserver: MutableMap<CollectionId, MutableLiveData<AppState>> =
        mutableMapOf(CollectionId.EMPTY to MutableLiveData<AppState>())

    fun getMoviesListLiveData(id: CollectionId): LiveData<AppState>? =
        moviesListLiveDataObserver.run {
            remove(CollectionId.EMPTY)
            put(id, MutableLiveData<AppState>())
            get(id)
        }

    fun getDataFromLocalSource(collectionId: String) {}


    fun getDataFromRemoteSource(collectionId: CollectionId) {
        moviesListLiveDataObserver[collectionId]?.postValue(AppState.Loading)

        collectionRepositoryImpl.getCollectionFromServer(
            collectionId,
            object : Callback<ListMoviesDTO> {

                override fun onResponse(
                    call: Call<ListMoviesDTO>,
                    response: Response<ListMoviesDTO>,
                ) {
                    val serverResponse: ListMoviesDTO? = response.body()

                    moviesListLiveDataObserver[collectionId]?.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            checkResponse(serverResponse)
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

                private fun checkResponse(serverResponse: ListMoviesDTO): AppState {
                    return AppState.SuccessMovies(convertDtoToModel(serverResponse))
                }
            })
    }


    private fun convertDtoToModel(listMoviesDTO: ListMoviesDTO): ListMovies {
        return ListMovies(
            page = listMoviesDTO.page,
            total_pages = listMoviesDTO.total_pages,
            total_results = listMoviesDTO.total_results,
            results = convertDtoToModel(listMoviesDTO.results)
        )
    }


    private fun convertDtoToModel(results: List<MovieDTO>?): List<Movie> {
        val listMovies: MutableList<Movie> = mutableListOf(Movie())
        results?.forEach {
            listMovies.add(
                Movie(
                    poster_path = it.poster_path,
                    vote_average = it.vote_average,
                    id = it.id,
                    title = it.title,
                )
            )
        }
        listMovies.removeAt(0)   //TODO
        return listMovies
    }


}