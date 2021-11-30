package com.example.onik.model.repository

import com.example.onik.model.data.ListMovies
import com.example.onik.viewmodel.CollectionId
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun getCollectionFromServer(collectionId: CollectionId, page: Int): Flow<ListMovies>
}
