package com.example.onik.model.localRepository

import com.example.onik.model.data.Movie
import com.example.onik.model.room.MovieEntity


fun convertMovieEntityToMovie(entityList: List<MovieEntity>): List<Movie> {
    return entityList.map {
        Movie(
            id = it.idMovie,
            note = it.note,
            title = it.title,
            vote_average = it.vote_average,
            poster_path = it.poster_path,
        )
    }
}

fun convertMovieToEntity(movie: Movie): MovieEntity {
    return MovieEntity(
        idMovie = movie.id!!,
        note = movie.note,
        title = movie.title,
        vote_average = movie.vote_average,
        poster_path = movie.poster_path,
    )
}
