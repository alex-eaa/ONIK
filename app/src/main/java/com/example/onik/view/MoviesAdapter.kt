package com.example.onik.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.Movie
import com.example.onik.model.MovieDTO

class MoviesAdapter(
    private val itemLayoutForInflate: Int,
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var moviesData: List<MovieDTO> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemViewClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieDTO) {
            itemView.apply {
                findViewById<TextView>(R.id.release_date).text = movie.release_date?.substring(0, 4)
                findViewById<TextView>(R.id.title).text = movie.title
                findViewById<TextView>(R.id.voteAverage).text = movie.vote_average.toString()
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
        fun onItemClick(movie: MovieDTO)
    }

}