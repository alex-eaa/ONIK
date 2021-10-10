package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.model.data.CastDTO
import com.example.onik.model.data.ListMovieCreditsDTO
import com.example.onik.model.data.convertCastDtoToCast
import com.example.onik.model.data.convertListMovieCreditsDtoToListMovies
import com.example.onik.model.repository.CastRepository
import com.example.onik.model.repository.CastRepositoryImpl
import com.example.onik.model.repository.MovieCreditsRepository
import com.example.onik.model.repository.MovieCreditsRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class PeopleViewModel : ViewModel() {


    private val castRepositoryImpl: CastRepository = CastRepositoryImpl()
    private val movieCreditsRepositoryImpl: MovieCreditsRepository = MovieCreditsRepositoryImpl()

    private val peopleLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()
    val peopleLiveData: LiveData<AppState> = peopleLiveDataObserver

    private val movieCreditsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()
    val movieCreditsLiveData: LiveData<AppState> = movieCreditsLiveDataObserver



    fun getDataFromRemoteSource(idPeople: Int) {
        peopleLiveDataObserver.value = AppState.Loading
        castRepositoryImpl.getCastFromServer(idPeople, callBackPeople)
        movieCreditsRepositoryImpl.getMovieCreditsFromServer(idPeople, callBackMovieCredits)
    }

    private val callBackPeople = object : Callback<CastDTO> {
        override fun onResponse(call: Call<CastDTO>, response: Response<CastDTO>) {
            val serverResponse: CastDTO? = response.body()

            peopleLiveDataObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    AppState.SuccessCast(convertCastDtoToCast(serverResponse))
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<CastDTO>, t: Throwable) {
            Log.d(TAG, t.message.toString())
            peopleLiveDataObserver.postValue(AppState.Error(Throwable(t.message
                ?: REQUEST_ERROR)))
        }
    }

    private val callBackMovieCredits = object : Callback<ListMovieCreditsDTO> {
        override fun onResponse(call: Call<ListMovieCreditsDTO>, response: Response<ListMovieCreditsDTO>) {
            val serverResponse: ListMovieCreditsDTO? = response.body()

            movieCreditsLiveDataObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    AppState.SuccessMovies(convertListMovieCreditsDtoToListMovies(serverResponse))
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<ListMovieCreditsDTO>, t: Throwable) {
            Log.d(TAG, t.message.toString())
            movieCreditsLiveDataObserver.postValue(AppState.Error(Throwable(t.message
                ?: REQUEST_ERROR)))
        }
    }

}