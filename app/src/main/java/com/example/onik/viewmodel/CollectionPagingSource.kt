package com.example.onik.viewmodel

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.onik.model.data.ListMovies
import com.example.onik.model.data.ListMoviesDTO
import com.example.onik.model.data.Movie
import com.example.onik.model.data.convertListMoviesDtoToListMovies
import com.example.onik.model.repository.RemoteDataSourceCollections
import retrofit2.HttpException
import java.io.IOException


class CollectionPagingSource(
    private val service: RemoteDataSourceCollections,
    private val collectionId: CollectionId
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        Log.d("Paging", "page = $page")
        val pageSize = params.loadSize
        try {
            val response: ListMoviesDTO = service.getCollection(collectionId, page)
            val repos: List<Movie> = convertListMoviesDtoToListMovies(response).results!!

            val nextKey = if (repos.size < pageSize) null else page + 1
            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            Log.d("Paging", "nextKey = $nextKey, prevKey = $prevKey")

            return LoadResult.Page(
                data = repos,
                prevKey = nextKey,
                nextKey = prevKey
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
