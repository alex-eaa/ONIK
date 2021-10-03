package com.example.onik.model.repository

import com.example.onik.model.data.ListCastsDTO
import retrofit2.Callback


interface CastsRepository {
    fun getListCastsFromServer(movieId: Int, callback: Callback<ListCastsDTO>)
}
