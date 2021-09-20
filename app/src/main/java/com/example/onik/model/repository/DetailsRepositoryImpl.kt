package com.example.onik.model.repository

import com.example.onik.model.data.MovieDTO


class DetailsRepositoryImpl(private val remoteDataSourceDetails: RemoteDataSourceDetails) :
    DetailsRepository {

    override fun getMovieDetailsFromServer(
        movieId: Int,
        callback: retrofit2.Callback<MovieDTO>,
    ) {
        remoteDataSourceDetails.getMovieDetails(movieId, callback)
    }

}