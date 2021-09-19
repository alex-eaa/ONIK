package com.example.onik.model

import okhttp3.Callback

interface DetailsRepository {
    fun getMovieDetailsFromServer(requestLink: String, callback: Callback)
}