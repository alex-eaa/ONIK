package com.example.onik.model

import com.example.onik.Foo


class RepositoryImpl : Repository {
    override fun getMovieFromServer(id: Int): Movie {
        for (movie in Foo.movies) {
            if (movie.id == id) {
                return movie
            }
        }
        return Movie()
    }

    override fun getMovieFromLocalStorage(id: Int): Movie {
        for (movie in Foo.movies) {
            if (movie.id == id) {
                return movie
            }
        }
        return Movie()
    }

    override fun getPopularMoviesFromServer(): Array<Movie> {
        return Foo.movies
    }

    override fun getPopularMoviesFromLocalStorage(): Array<Movie> {
        return Foo.movies
    }


}