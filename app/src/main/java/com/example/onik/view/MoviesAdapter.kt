package com.example.onik.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.Movie

class MoviesAdapter(
    private val itemLayoutForInflate: Int,
    private val onItemClicked: (position: Int) -> Unit,
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    var moviesData: List<Movie> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(
        itemView: View,
        private val onItemClicked: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val release_date: TextView = itemView.findViewById(R.id.release_date)
        val title: TextView = itemView.findViewById(R.id.title)
        val vote_average: TextView = itemView.findViewById(R.id.voteAverage)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(adapterPosition)
        }
    }


    // RecyclerView вызывает этот метод всякий раз, когда ему нужно создать новый ViewHolder.
    // Метод создает и инициализирует ViewHolder и связанный с ним View,
    // но не заполняет содержимое представления - ViewHolder еще не привязан к конкретным данным.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)

        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Получить элемент из вашего набора данных в этой позиции и заменить
        // содержимое представления в этой позици этим элементом
        viewHolder.release_date.text = moviesData[position].release_date.substring(0, 4)
        viewHolder.title.text = moviesData[position].title
        viewHolder.vote_average.text = moviesData[position].vote_average.toString()
    }

    override fun getItemCount() = moviesData.size

}