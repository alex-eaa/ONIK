package com.example.onik.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.onik.R
import com.example.onik.model.data.Cast
import com.example.onik.model.data.Movie
import com.squareup.picasso.Picasso

class CastsAdapter(
    private val itemLayoutForInflate: Int,
) :
    RecyclerView.Adapter<CastsAdapter.ViewHolder>() {

    var data: List<Cast> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemViewClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cast: Cast) {
            itemView.apply {
                findViewById<TextView>(R.id.actorName).text = cast.name

                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500/${cast.profile_path}")
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .into(findViewById<ImageView>(R.id.actorPhoto))

                setOnClickListener {
                    listener?.onItemClick(cast)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(itemLayoutForInflate, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size


    fun interface OnItemViewClickListener {
        fun onItemClick(cast: Cast)
    }

}