package com.example.onik.model.data

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


// Credits
// https://api.themoviedb.org/3/movie/436969/credits?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU