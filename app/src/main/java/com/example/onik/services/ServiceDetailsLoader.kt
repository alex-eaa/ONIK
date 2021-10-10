package com.example.onik.services

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.onik.BuildConfig
import com.example.onik.model.data.MovieDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val TAG = "ServiceDetailsLoader"
const val DETAILS_INTENT_FILTER = "DETAILS_INTENT_FILTER"
const val MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA"
const val DETAILS_EXTRA = "DETAILS_EXTRA"
const val RESULT_EXTRA = "RESULT_EXTRA"
const val ERROR_EXTRA = "ERROR_EXTRA"
const val SUCCESS_RESULT = "SUCCESS_RESULT"
const val ERROR_RESULT = "ERROR_RESULT"

class ServiceDetailsLoader(name: String = "ServiceDetailsLoader") : IntentService(name) {

    private val api_key = BuildConfig.THEMOVIEDB_API_KEY


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.getIntExtra(MOVIE_ID_EXTRA, -1).let { id ->
            id?.let { loadDetails(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadDetails(movieId: Int) {

        val uri: URL = try {
            URL("https://api.themoviedb.org/3/movie/${movieId}?api_key=${api_key}&language=ru-RU")

        } catch (e: MalformedURLException) {
            onErrorResponse("Url error")
            return
        }

        var urlConnection: HttpsURLConnection? = null

        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
            }

            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val movieDTO: MovieDTO = Gson().fromJson(getLines(br), MovieDTO::class.java)
            onSuccessResponse(movieDTO)

        } catch (e: Exception) {
//            onErrorResponse(e.message ?: "Unknown error")
            onErrorResponse(e.message.toString())

        } finally {
            urlConnection?.disconnect()
        }
    }


    private fun onSuccessResponse(movieDTO: MovieDTO) {

        // Отправляем Broadcast
//        sendBroadcast(
//            Intent(DETAILS_INTENT_FILTER)
//                .putExtra(RESULT_EXTRA, SUCCESS_RESULT)
//                .putExtra(DETAILS_EXTRA, movieDTO)
//        )

        // Отправляем локальный Broadcast
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, SUCCESS_RESULT)
                    .putExtra(DETAILS_EXTRA, movieDTO)
            )

    }

    private fun onErrorResponse(error: String) {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, ERROR_RESULT)
                    .putExtra(ERROR_EXTRA, error)
            )
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    override fun onDestroy() {
        Log.d(TAG, "ServiceDetailsLoader onDestroy")
        super.onDestroy()
    }

}