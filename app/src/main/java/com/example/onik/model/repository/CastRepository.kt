package com.example.onik.model.repository

import com.example.onik.model.data.CastDTO
import com.example.onik.model.data.ListCastsDTO
import retrofit2.Callback


interface CastRepository {
    fun getCastFromServer(peopleId: Int, callback: Callback<CastDTO>)
}
