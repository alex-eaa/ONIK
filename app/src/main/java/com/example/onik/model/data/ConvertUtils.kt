package com.example.onik.model.data

import com.example.onik.model.room.MovieEntity


fun convertListMoviesDtoToListMovies(listMoviesDTO: ListMoviesDTO): ListMovies {
    return ListMovies(
        page = listMoviesDTO.page,
        total_pages = listMoviesDTO.total_pages,
        total_results = listMoviesDTO.total_results,
        results = listMoviesDTO.results?.map { convertMovieDtoToMovieForCard(it) }
    )
}


fun convertMovieDtoToMovieForCard(movieDTO: MovieDTO): Movie {
    return Movie(
        poster_path = movieDTO.poster_path,
        vote_average = movieDTO.vote_average,
        id = movieDTO.id,
        title = movieDTO.title,
    )
}


fun convertMovieDtoToMovie(movieDTO: MovieDTO): Movie {
    return Movie(
        poster_path = movieDTO.poster_path,
        adult = movieDTO.adult,
        overview = movieDTO.overview,
        release_date = movieDTO.release_date,
        id = movieDTO.id,
        title = movieDTO.title,
        backdrop_path = movieDTO.backdrop_path,
        popularity = movieDTO.popularity,
        vote_count = movieDTO.vote_count,
        vote_average = movieDTO.vote_average,
        runtime = movieDTO.runtime,
        budget = movieDTO.budget,
        revenue = movieDTO.revenue,
        genres = movieDTO.genres?.map { convertMovieGenreDtoToMovieGenre(it) }
    )
}


private fun convertMovieGenreDtoToMovieGenre(genreDTO: MovieDTO.GenresDTO): Movie.Genre {
    return Movie.Genre(
        id = genreDTO.id,
        name = genreDTO.name
    )
}

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
