package com.example.onik.model

import com.example.onik.viewmodel.CollectionId

interface CollectionRepository {
    fun getCollectionFromServer(collection: CollectionId, callback: retrofit2.Callback<ListMoviesDTO>)
}
