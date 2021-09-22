package com.example.onik.model.localRepository

import com.example.onik.model.data.Movie
import com.example.onik.model.room.MovieDao

class LocalRepositoryImpl (private val localDataSource: MovieDao) : LocalRepository{
    override fun getAllMovie(): List<Movie> {
        return convertMovieEntityToMovie(localDataSource.all())
    }

    override fun getMovieNote(movieId: Int): String {
        return localDataSource.getNote(movieId)
    }

    override fun saveMovie(movie: Movie) {
        localDataSource.insert(convertMovieToEntity(movie))
    }
}