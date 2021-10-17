package com.example.onik.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.data.Movie
import com.squareup.picasso.Picasso

class MoviesAdapterForPaging(
    private val itemLayoutForInflate: Int,
) :
    PagingDataAdapter<Movie, MoviesAdapterForPaging.ViewHolder>(ArticleDiffItemCallback) {

    var listener: OnItemViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie?) {
            movie?.let {
                itemView.apply {
                    it.release_date?.let { release_date ->
                        if (release_date.length > 4) {
                            findViewById<TextView>(R.id.release_date).text =
                                release_date.substring(0, 4)
                        }
                    }
                    findViewById<TextView>(R.id.title).text = it.title
                    findViewById<TextView>(R.id.voteAverage).text = it.vote_average.toString()

                    Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500/${it.poster_path}")
                        .placeholder(R.drawable.placeholder)
                        .fit()
                        .into(findViewById<ImageView>(R.id.poster))

                    setOnClickListener { view ->
                        listener?.onItemClick(it)
                    }
                }
            }
        }
    }

    fun interface OnItemViewClickListener {
        fun onItemClick(movie: Movie)
    }

}


private object ArticleDiffItemCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.title == newItem.title && oldItem.overview == newItem.overview
    }

}
