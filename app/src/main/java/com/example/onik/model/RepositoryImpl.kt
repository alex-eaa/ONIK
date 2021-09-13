package com.example.onik.model

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.onik.BuildConfig
import com.example.onik.Foo
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.Constants
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class RepositoryImpl : Repository {

    override fun getMovieDetailsFromLocalStorage(id: Int): Movie = Foo.movies.first { it.id == id }

    override fun getListMoviesFromLocalSource(): List<Movie> = Foo.movies


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovieDetailsFromServer(movieId: Int, liveData: MutableLiveData<AppState>) {
        liveData.postValue(AppState.Loading)

        val onLoadListener: MovieLoader.MovieLoaderListener =
            object : MovieLoader.MovieLoaderListener {

                override fun onLoaded(movieDTO: MovieDTO) {
                    liveData.postValue(AppState.SuccessMovie(movieDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(AppState.Error(throwable))
                }
            }

        MovieLoader(onLoadListener, movieId).loadData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getListMoviesFromServer(collectionId: String, liveData: MutableLiveData<AppState>) {
        liveData.postValue(AppState.Loading)

        val onLoadListener: ListMoviesLoader.ListMoviesLoaderListener =
            object : ListMoviesLoader.ListMoviesLoaderListener {

                override fun onLoaded(listMoviesDTO: ListMoviesDTO) {
                    liveData.postValue(AppState.SuccessMovies(listMoviesDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(AppState.Error(throwable))
                }
            }

        ListMoviesLoader(onLoadListener, collectionId).loadData()
    }

}
