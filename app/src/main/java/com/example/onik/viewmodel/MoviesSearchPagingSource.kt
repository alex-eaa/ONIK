package com.example.onik.viewmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.Movie
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.model.repository.RemoteDataSourceSearch
import retrofit2.HttpException
import java.io.IOException


class MoviesSearchPagingSource(
    private val service: RemoteDataSourceSearch,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        val pageSize = params.loadSize
        try {
            val response: ListMoviesDTO = service.getFind(query, page)
            val repos: List<Movie> = convertListMoviesDtoToListMovies(response).results!!

            val nextKey = if (repos.size < pageSize) null else page + 1
            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1

            return LoadResult.Page(
                data = repos,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }

}
