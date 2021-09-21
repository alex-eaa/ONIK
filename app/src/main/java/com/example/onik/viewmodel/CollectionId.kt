package com.example.onik.viewmodel

enum class CollectionId (val id: String, val description: String){
    POPULAR ("popular", "Популярные"),
    TOP_RATED("top_rated", "Лучшие"),
    NOW_PLAYING("now_playing", "Смотрят сейчас"),
    UPCOMING("upcoming", "Ожидаемые"),
    FIND("find", "Результаты поиска"),

    EMPTY("empty", "")
}