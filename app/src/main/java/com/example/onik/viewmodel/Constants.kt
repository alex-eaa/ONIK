package com.example.onik.viewmodel

interface Constants {

    companion object {
        const val MOVIES_COLLECTION_1 = "POPULAR"
        const val MOVIES_COLLECTION_2 = "TOP_RATED"
        const val MOVIES_COLLECTION_3 = "NOW_PLAYING"

        const val API_KEY = "be47b00f04df8db4b32e99ad4fdbe004"
    }
}


// https://api.themoviedb.org/3/movie/436969?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS
// https://api.themoviedb.org/3/movie/popular?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS&page=1
// https://api.themoviedb.org/3/movie/top_rated?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU&page=1
// https://api.themoviedb.org/3/movie/now_playing?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU&page=1