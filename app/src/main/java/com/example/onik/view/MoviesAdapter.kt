package com.example.onik.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.data.Movie
import com.squareup.picasso.Picasso

class MoviesAdapter(
    private val itemLayoutForInflate: Int,
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var moviesData: List<Movie> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemViewClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.apply {
                findViewById<TextView>(R.id.release_date).text = movie.release_date?.substring(0, 4)
                findViewById<TextView>(R.id.title).text = movie.title
                findViewById<TextView>(R.id.voteAverage).text = movie.vote_average.toString()

                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .into(findViewById<ImageView>(R.id.poster))

                setOnClickListener {
                    listener?.onItemClick(movie)
                }
            }
        }
    }


    // RecyclerView вызывает этот метод всякий раз, когда ему нужно создать новый ViewHolder.
    // Метод создает и инициализирует ViewHolder и связанный с ним View,
    // но не заполняет содержимое представления - ViewHolder еще не привязан к конкретным данным.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Получить элемент из вашего набора данных в этой позиции и заменить
        // содержимое представления в этой позици этим элементом
        viewHolder.bind(moviesData[position])
    }

    override fun getItemCount() = moviesData.size


    fun interface OnItemViewClickListener {
        fun onItemClick(movie: Movie)
    }

}