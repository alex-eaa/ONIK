package com.example.onik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesCollectionViewModelFactory(
    private val collectionId: CollectionId
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesCollectionViewModel(collectionId) as T
    }
}
