package com.example.onik.model

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class RemoteDataSource {

    fun getMovieDetails(requestLink: String, callback: Callback) {
        val builder: Request.Builder = Request.Builder().apply {
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }
}