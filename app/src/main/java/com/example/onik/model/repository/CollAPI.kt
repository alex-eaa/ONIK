package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface CollAPI {

    @GET("3/movie/{collection}")
    fun getColl(
        @Path("collection") collection: String,
        @Query("language") language: String,
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
    ): Single<ListMoviesDTO>


    companion object {
        fun getCollRetrofit() = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(CollAPI::class.java)
    }
}