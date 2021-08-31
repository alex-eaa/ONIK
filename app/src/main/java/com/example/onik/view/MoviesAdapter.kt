package com.example.onik.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.Movie

class MoviesAdapter(
    private val dataSet: Array<Movie>,
    private val onItemClicked: (position: Int) -> Unit
) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {


    class ViewHolder(
        itemView: View,
        private val onItemClicked: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val release_date: TextView = itemView.findViewById(R.id.release_date)
        val title: TextView = itemView.findViewById(R.id.title)
        val overview: TextView = itemView.findViewById(R.id.overview)

        init {
            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onItemClicked(adapterPosition)
        }


    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item, viewGroup, false)

        return ViewHolder(view, onItemClicked)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.release_date.text = dataSet[position].release_date
        viewHolder.title.text = dataSet[position].title
        viewHolder.overview.text = dataSet[position].overview

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}