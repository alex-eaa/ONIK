package com.example.onik.model

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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


class RepositoryImpl : Repository, Constants {
    companion object {
        const val TAG = "RepositoryImpl"
        const val TYPE_DATA_MOVIE = "MOVIE"
        const val TYPE_DATA_COLLECTION = "COLLECTION"
    }

    override fun getMovieDetailsFromLocalStorage(id: Int): Movie = Foo.movies.first { it.id == id }

    override fun getListMoviesFromLocalSource(): List<Movie> = Foo.movies


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovieDetailsFromServer(id: Int): AppState {
        return getFromInternet(createUri(movieId = id), TYPE_DATA_MOVIE)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getListMoviesFromServer(collectionId: String): AppState {
        return getFromInternet(createUri(collectionId = collectionId), TYPE_DATA_COLLECTION)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getFromInternet(uri: URL, typeData: String): AppState {
        Log.d(TAG, uri.toString())

        var urlConnection: HttpsURLConnection? = null

        return try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))

            when (typeData) {
                TYPE_DATA_COLLECTION -> AppState.SuccessMovies(Gson().fromJson(result,
                    ListMoviesDTO::class.java))
                TYPE_DATA_MOVIE -> AppState.SuccessMovie(Gson().fromJson(result,
                    MovieDTO::class.java))
                else -> {
                    throw JsonSyntaxException("FAILED parsing")
                }
            }

        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "FAILED parsing", e)
            AppState.Error(e)

        } catch (e: Exception) {
            Log.e(TAG, "FAILED connect", e)
            AppState.Error(e)

        } finally {
            urlConnection?.disconnect()
        }
    }


    private fun createUri(
        movieId: Int? = null,
        collectionId: String? = null,
    ): URL {
        return URL(
            Uri.Builder().apply {
                scheme("https")
                authority("api.themoviedb.org")
                appendPath("3")
                appendPath("movie")
                movieId?.let { appendPath(movieId.toString()) }
                collectionId?.let { appendPath(collectionId) }
                appendQueryParameter("api_key", BuildConfig.THEMOVIEDB_API_KEY)
                appendQueryParameter("language", "ru-RU")
                movieId?.let { appendQueryParameter("page", "1") }
            }.build().toString()
        )
    }

}
