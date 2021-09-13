package com.example.onik.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.onik.Foo
import com.example.onik.viewmodel.AppState
import com.example.onik.viewmodel.CollectionId


class RepositoryImpl : Repository {

    override fun getMovieDetailsFromLocalStorage(id: Int): Movie = Foo.movies.first { it.id == id }

    override fun getListMoviesFromLocalSource(): List<Movie> = Foo.movies


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovieDetailsFromServer(id: Int, liveData: MutableLiveData<AppState>) {
        liveData.postValue(AppState.Loading)

        val onLoadListener: MovieLoader.MovieLoaderListener =
            object : MovieLoader.MovieLoaderListener {

                override fun onLoaded(movieDTO: MovieDTO) {
                    liveData.postValue(AppState.SuccessMovie(movieDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(AppState.Error(throwable))
                }
            }

        MovieLoader(onLoadListener, id).loadData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getListMoviesFromServer(id: CollectionId, liveData: MutableLiveData<AppState>) {
        liveData.postValue(AppState.Loading)

        val onLoadListener: ListMoviesLoader.ListMoviesLoaderListener =
            object : ListMoviesLoader.ListMoviesLoaderListener {

                override fun onLoaded(listMoviesDTO: ListMoviesDTO) {
                    liveData.postValue(AppState.SuccessMovies(listMoviesDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(AppState.Error(throwable))
                }
            }

        ListMoviesLoader(onLoadListener, id).loadData()
    }

}
