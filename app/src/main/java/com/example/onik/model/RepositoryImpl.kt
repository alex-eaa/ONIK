package com.example.onik.model

import com.example.onik.Foo


class RepositoryImpl : Repository {

    // https://api.themoviedb.org/3/movie/436969?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS
    override fun getMovieDetailsFromServer(id: Int): Movie = Foo.movies.first { it.id == id }

    override fun getMovieDetailsFromLocalStorage(id: Int): Movie = Foo.movies.first { it.id == id }

    // https://api.themoviedb.org/3/movie/popular?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS&page=1
    // https://api.themoviedb.org/3/movie/top_rated?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU&page=1
    // https://api.themoviedb.org/3/movie/now_playing?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RU&page=1
    override fun getListMoviesFromRemoteSource(): List<Movie> = Foo.movies

    override fun getListMoviesFromLocalSource(): List<Movie> = Foo.movies
}