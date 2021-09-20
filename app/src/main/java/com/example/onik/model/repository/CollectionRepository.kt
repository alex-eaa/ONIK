package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId

interface CollectionRepository {
    fun getCollectionFromServer(collection: CollectionId, callback: retrofit2.Callback<ListMoviesDTO>)
}
