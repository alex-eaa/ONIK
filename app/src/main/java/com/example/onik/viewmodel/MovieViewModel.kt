package com.example.onik.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onik.BuildConfig
import com.example.onik.model.*
import com.example.onik.model.data.Movie
import com.example.onik.model.data.MovieDTO
import com.example.onik.model.repository.DetailsRepository
import com.example.onik.model.repository.DetailsRepositoryImpl
import com.example.onik.model.repository.RemoteDataSourceDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ViewModel"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val api_key = BuildConfig.THEMOVIEDB_API_KEY

class MovieViewModel : ViewModel() {

    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(
        RemoteDataSourceDetails())

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()

    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver


    fun getDataFromLocalSource(id: Int) {}


    fun getDataFromRemoteSource(movieId: Int) {
        movieDetailsLiveDataObserver.value = AppState.Loading
        detailsRepositoryImpl.getMovieDetailsFromServer(movieId, callBack)
    }

    private val callBack = object : Callback<MovieDTO> {
//        @Throws(IOException::class)

        override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
            val serverResponse: MovieDTO? = response.body()

            movieDetailsLiveDataObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
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

        private fun checkResponse(serverResponse: MovieDTO): AppState {
            return AppState.SuccessMovie(convertDtoToModel(serverResponse))
        }
    }


    private fun convertDtoToModel(movieDTO: MovieDTO): Movie {
        return Movie(
            poster_path = movieDTO.poster_path,
            adult = movieDTO.adult,
            overview = movieDTO.overview,
            release_date = movieDTO.release_date,
            id = movieDTO.id,
            title = movieDTO.title,
            backdrop_path = movieDTO.backdrop_path,
            popularity = movieDTO.popularity,
            vote_count = movieDTO.vote_count,
            vote_average = movieDTO.vote_average,
            runtime = movieDTO.runtime,
            budget = movieDTO.budget,
            revenue = movieDTO.revenue,
            genres = convertDtoToModel(movieDTO.genres)
        )
    }

    private fun convertDtoToModel(genresDTO: List<MovieDTO.GenresDTO>?): List<Movie.Genre> {
        val listGenres: MutableList<Movie.Genre> = mutableListOf(Movie.Genre())
        genresDTO?.forEach {
            listGenres.add(Movie.Genre(it.id, it.name))
        }
        listGenres.removeAt(0)   //TODO
        return listGenres
    }

}