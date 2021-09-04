package com.example.onik.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.Movie

class MoviesAdapter(
    private val onItemClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var dataSet: Array<Movie> = arrayOf()


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

    fun setData(data: Array<Movie>){
        dataSet = data
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)

        return ViewHolder(view, onItemClicked)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.release_date.text = dataSet[position].release_date.substring(0, 4)
        viewHolder.title.text = dataSet[position].title
        viewHolder.vote_average.text = dataSet[position].vote_average.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}