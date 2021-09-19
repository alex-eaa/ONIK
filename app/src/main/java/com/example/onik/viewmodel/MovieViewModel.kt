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

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val api_key = BuildConfig.THEMOVIEDB_API_KEY

class MovieViewModel : ViewModel() {

    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())

    private val movieDetailsLiveDataObserver: MutableLiveData<AppState> =
        MutableLiveData<AppState>()

    val movieDetailsLiveData: LiveData<AppState> = movieDetailsLiveDataObserver


    fun getDataFromLocalSource(id: Int) {}


    fun getDataFromRemoteSource(movieId: Int) {
        val requestLink =
            "https://api.themoviedb.org/3/movie/${movieId}?api_key=${api_key}&language=ru-RU"
        movieDetailsLiveDataObserver.value = AppState.Loading
        detailsRepositoryImpl.getMovieDetailsFromServer(requestLink, callBack)
    }

    private val callBack = object : Callback {
        @Throws(IOException::class)

        override fun onResponse(call: Call?, response: Response) {
            val serverResponse: String? = response.body()?.string()
            movieDetailsLiveDataObserver.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call?, e: IOException?) {
            movieDetailsLiveDataObserver.postValue(AppState.Error(Throwable(e?.message
                ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: String): AppState {
            val movieDTO: MovieDTO = Gson().fromJson(serverResponse, MovieDTO::class.java)
            return AppState.SuccessMovie(convertDtoToModel(movieDTO))
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