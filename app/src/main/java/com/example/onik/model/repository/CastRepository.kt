package com.example.onik.model.repository

import com.example.onik.model.data.CastDTO
import retrofit2.Callback


interface CastRepository {
    fun getCastFromServer(peopleId: Int, callback: Callback<CastDTO>)
}
