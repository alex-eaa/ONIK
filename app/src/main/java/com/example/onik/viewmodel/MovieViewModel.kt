package com.example.onik.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.onik.app.App.Companion.getMovieDao
import com.example.onik.model.data.MovieDTO
import com.example.onik.model.data.MovieLocal
import com.example.onik.model.data.convertMovieDtoToMovie
import com.example.onik.model.localRepository.LocalRepository
import com.example.onik.model.localRepository.LocalRepositoryImpl
import com.example.onik.model.repository.DetailsRepository
import com.example.onik.model.repository.DetailsRepositoryImpl
import com.example.onik.model.repository.RemoteDataSourceDetails
import com.example.onik.model.room.MovieEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(
        RemoteDataSourceDetails())

    private val localRepository: LocalRepository = LocalRepositoryImpl(getMovieDao())

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()
    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver

    fun getNoteLiveData(movieId: Int): LiveData<MovieEntity> {
        return localRepository.getMovieLiveData(movieId)
    }


    fun saveNoteToDB(movieEntity: MovieEntity) {
        Observable
            .fromCallable { localRepository.saveMovie(movieEntity) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }


    fun getDataFromRemoteSource(movieId: Int) {
        movieDetailsLiveDataObserver.value = AppState.Loading
        detailsRepositoryImpl.getMovieDetailsFromServer(movieId, callBack)
    }


    private val callBack = object : Callback<MovieDTO> {
        override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
            val serverResponse: MovieDTO? = response.body()

            movieDetailsLiveDataObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    AppState.SuccessMovie(convertMovieDtoToMovie(serverResponse))
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
            Log.d(TAG, t.message.toString())
            movieDetailsLiveDataObserver.postValue(AppState.Error(Throwable(t.message
                ?: REQUEST_ERROR)))
        }
    }
}