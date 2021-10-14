package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Callback

interface CollRepository {
    fun getCollFromServer(collection: CollectionId, page: Int): Single<ListMoviesDTO>
}
