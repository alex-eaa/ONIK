package com.example.onik.model

data class Genre(
    val id: Int,
    val name: String,
)

// Получить список всех Genre
// https://api.themoviedb.org/3/genre/movie/list?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU