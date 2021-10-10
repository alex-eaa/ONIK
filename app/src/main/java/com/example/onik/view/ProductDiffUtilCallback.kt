package com.example.onik.view

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.onik.model.data.Movie

class ProductDiffUtilCallback(
    private val oldList: List<Movie>,
    private val newList: List<Movie>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie: Movie = oldList[oldItemPosition]
        val newMovie: Movie = newList[newItemPosition]
        Log.d("ProductDiffUtilCallback", (oldMovie.id == newMovie.id).toString())
        return oldMovie.id == newMovie.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMovie: Movie = oldList[oldItemPosition]
        val newMovie: Movie = newList[newItemPosition]
        Log.d("ProductDiffUtilCallback", (oldMovie == newMovie).toString())
        return oldMovie == newMovie
    }
}