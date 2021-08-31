package com.example.onik.model

import com.example.onik.Temp


class RepositoryImpl : Repository {
    override fun getMovieFromServer(): Movie {
        return Temp.movie1
    }

    override fun getMovieFromLocalStorage(): Movie {
        return Temp.movie1
    }

    override fun getPopularMoviesFromServer(): Array<Movie> {
        return arrayOf(Temp.movie1, Temp.movie2, Temp.movie3, Temp.movie4)
    }

    override fun getPopularMoviesFromLocalStorage(): Array<Movie> {
        return arrayOf(Temp.movie1, Temp.movie2, Temp.movie3, Temp.movie4)
    }


}