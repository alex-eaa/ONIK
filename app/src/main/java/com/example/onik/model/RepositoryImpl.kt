package com.example.onik.model

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.onik.Foo
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.Constants
import com.example.onik.viewmodel.Constants.Companion.API_KEY
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class RepositoryImpl : Repository, Constants {
    private val TAG = "RepositoryImpl"

    override fun getMovieDetailsFromLocalStorage(id: Int): Movie = Foo.movies.first { it.id == id }

    override fun getListMoviesFromLocalSource(): List<Movie> = Foo.movies


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovieDetailsFromServer(id: Int) : AppState {
        val uriBuilder: Uri.Builder = Uri.Builder().apply {
            scheme("https")
            authority("api.themoviedb.org")
            appendPath("3")
            appendPath("movie")
            appendPath(id.toString())
            appendQueryParameter("api_key", API_KEY)
            appendQueryParameter("language", "ru-RU")
        }

        return getDetailsMovieFromInternet(URL(uriBuilder.build().toString()))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getListMoviesFromServer(collectionId: String): AppState{
        val uriBuilder: Uri.Builder = Uri.Builder().apply {
            scheme("https")
            authority("api.themoviedb.org")
            appendPath("3")
            appendPath("movie")
            appendPath(collectionId)
            appendQueryParameter("api_key", API_KEY)
            appendQueryParameter("language", "ru-RU")
            appendQueryParameter("page", "1")
        }

        return getListMoviesFromInternet(URL(uriBuilder.build().toString()))
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getListMoviesFromInternet(uri: URL): AppState {
        Log.d(TAG, uri.toString())

        var urlConnection: HttpsURLConnection? = null

        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))
            val data: ListMoviesDTO? = Gson().fromJson(result, ListMoviesDTO::class.java)
            return AppState.SuccessMovies(data)

        } catch (e: Exception) {
            Log.e(TAG, "FAILED", e)
            return AppState.Error(e)

        } finally {
            urlConnection?.disconnect()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getDetailsMovieFromInternet(uri: URL): AppState {
        Log.d(TAG, uri.toString())

        var urlConnection: HttpsURLConnection? = null

        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))
            val data: MovieDTO? = Gson().fromJson(result, MovieDTO::class.java)
            return AppState.SuccessMovie(data)

        } catch (e: Exception) {
            Log.e(TAG, "FAILED", e)
            return AppState.Error(e)

        } finally {
            urlConnection?.disconnect()
        }
    }
}
