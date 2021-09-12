package com.example.onik.viewmodel

interface Constants {

    companion object {
        const val MOVIES_COLLECTION_1 = "popular"
        const val MOVIES_COLLECTION_2 = "top_rated"
        const val MOVIES_COLLECTION_3 = "now_playing"
        const val MOVIES_COLLECTION_4 = "upcoming"
    }
}


// https://api.themoviedb.org/3/movie/436969?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS
// https://api.themoviedb.org/3/movie/popular?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS&page=1
// https://api.themoviedb.org/3/movie/top_rated?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU&page=1
// https://api.themoviedb.org/3/movie/now_playing?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU&page=1