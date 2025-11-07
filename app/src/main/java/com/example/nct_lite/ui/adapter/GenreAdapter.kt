package com.example.nct_lite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.model.Genre

class GenreAdapter(
    private val genres: List<Genre>,
    private val onItemClick: (Genre) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    inner class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvGenreName: TextView = view.findViewById(R.id.tv_genre_name)

        fun bind(genre: Genre) {
            tvGenreName.text = genre.name
            itemView.setOnClickListener { onItemClick(genre) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount(): Int = genres.size
}
