package com.example.onik.model

import com.example.onik.Foo


class RepositoryImpl : Repository {

    // https://api.themoviedb.org/3/movie/436969?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS
    override fun getMovieDetailsFromServer(id: Int): Movie {
        for (movie in Foo.movies) {
            if (movie.id == id) {
                return movie
            }
        }
        return Movie()
    }

    override fun getMovieDetailsFromLocalStorage(id: Int): Movie {
        for (movie in Foo.movies) {
            if (movie.id == id) {
                return movie
            }
        }
        return Movie()
    }

    // https://api.themoviedb.org/3/movie/popular?api_key=be47b00f04df8db4b32e99ad4fdbe004&language=ru-RUS&page=1
    override fun getPopularMoviesFromServer(): Array<Movie> {
        return Foo.movies
    }

    override fun getPopularMoviesFromLocalStorage(): Array<Movie> {
        return Foo.movies
    }


}