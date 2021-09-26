package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import retrofit2.Callback

interface CollectionRepository {
    fun getCollectionFromServer(collection: CollectionId, callback: Callback<ListMoviesDTO>)
}
