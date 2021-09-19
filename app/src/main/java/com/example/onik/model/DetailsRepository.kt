package com.example.onik.model



interface DetailsRepository {
    fun getMovieDetailsFromServer(movieId: Int, callback: retrofit2.Callback<MovieDTO>)
}
