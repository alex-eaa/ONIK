package com.example.onik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.BuildConfig
import com.example.onik.model.*
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.net.URL

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val api_key = BuildConfig.THEMOVIEDB_API_KEY

class MoviesCollectionViewModel : ViewModel() {

    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())

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
        val requestLink =
            "https://api.themoviedb.org/3/movie/${collectionId.id}?api_key=${api_key}&language=ru-RU"

        moviesListLiveDataObserver[collectionId]?.postValue(AppState.Loading)

        detailsRepositoryImpl.getMovieDetailsFromServer(
            requestLink,
            object : Callback {
                @Throws(IOException::class)

                override fun onResponse(call: Call?, response: Response) {
                    val serverResponse: String? = response.body()?.string()
                    moviesListLiveDataObserver[collectionId]?.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            checkResponse(serverResponse)
                        } else {
                            AppState.Error(Throwable(SERVER_ERROR))
                        }
                    )
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    moviesListLiveDataObserver[collectionId]?.postValue(AppState.Error(Throwable(e?.message
                        ?: REQUEST_ERROR)))
                }

                private fun checkResponse(serverResponse: String): AppState {
                    val listMoviesDTO: ListMoviesDTO = Gson().fromJson(serverResponse, ListMoviesDTO::class.java)
                    return AppState.SuccessMovies(listMoviesDTO)
                }
            })
    }


}