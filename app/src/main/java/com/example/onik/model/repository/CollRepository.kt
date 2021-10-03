package com.example.onik.model.repository

import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.viewmodel.CollectionId
import retrofit2.Callback

interface CollRepository {
    fun getCollFromServer(collection: CollectionId, callback: Callback<ListMoviesDTO>, page: Int)
}
