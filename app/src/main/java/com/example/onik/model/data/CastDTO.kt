package com.example.onik.model.data

data class CastDTO(
    val id: Int,
    val name: String,
    val profile_path: String?,
    val character: String?,

    val birthday: String?,
    val place_of_birth: String?,
    val homepage: String?,
)