package com.example.nct_lite.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.squareup.picasso.Picasso

class ArtistAdapter(
    private val onArtistClick: (String) -> Unit
) : ListAdapter<ArtistAdapter.ArtistData, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback()) {
    data class ArtistData(
        val name: String,
        val coverUrl: String?
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        holder.bind(artist, onArtistClick)
    }

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.text_artist_name)
        private val imageView: ImageView = itemView.findViewById(R.id.image_artist)

        fun bind(artist: ArtistData, onArtistClick: (String) -> Unit) {
            nameView.text = artist.name
            if (!artist.coverUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(artist.coverUrl)
                    .placeholder(R.drawable.placeholder_artist)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.placeholder_artist)
            }
            itemView.setOnClickListener { onArtistClick(artist.name) }
        }
    }
}

class ArtistDiffCallback : DiffUtil.ItemCallback<ArtistAdapter.ArtistData>() {
    override fun areItemsTheSame(oldItem: ArtistAdapter.ArtistData, newItem: ArtistAdapter.ArtistData): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ArtistAdapter.ArtistData, newItem: ArtistAdapter.ArtistData): Boolean {
        return oldItem == newItem
    }
}