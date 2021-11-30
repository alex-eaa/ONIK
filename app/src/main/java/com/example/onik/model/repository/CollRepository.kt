package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import io.reactivex.Single

interface CollRepository {
    fun getCollFromServer(collection: CollectionId, page: Int): Single<ListMoviesDTO>
}
