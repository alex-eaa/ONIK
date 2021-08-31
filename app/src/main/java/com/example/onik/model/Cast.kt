package com.example.onik.model

data class Cast(
    val id: Int,
    val name: String,
    val known_for_department: String,
    val profile_path: String,
    val character: String,
)

fun getDefaultCast() = Cast(
    819,
    "Edward Norton",
    "Acting",
    "/5XBzD5WuTyVQZeS4VI25z2moMeY.jpg",
    "The Narrator"
)