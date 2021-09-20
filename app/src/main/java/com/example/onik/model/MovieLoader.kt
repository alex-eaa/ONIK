package com.example.onik.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.example.onik.BuildConfig
import com.example.onik.model.data.MovieDTO
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)
class MovieLoader(
    private val listener: MovieLoaderListener,
    private val movieId: Int,
) {
    private val TAG = "MovieLoader"
    private val api_key = BuildConfig.THEMOVIEDB_API_KEY

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadData() {
        val handler = Handler(Looper.getMainLooper())

        val uri: URL = try {
            URL("https://api.themoviedb.org/3/movie/${movieId}?api_key=${api_key}&language=ru-RU")

        } catch (e: MalformedURLException) {
            handler.post { listener.onFailed(e) }
            return
        }

        Thread {
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000

                val br = BufferedReader(InputStreamReader(urlConnection.inputStream))

                Gson().fromJson(getLines(br), MovieDTO::class.java)
                    ?.let { movieDTO ->
                        handler.post { listener.onLoaded(movieDTO) }
                    } ?: throw JsonSyntaxException("Parsing error")

            } catch (e: Exception) {
                handler.post { listener.onFailed(e) }

            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    interface MovieLoaderListener {
        fun onLoaded(movieDTO: MovieDTO)
        fun onFailed(throwable: Throwable)
    }


}