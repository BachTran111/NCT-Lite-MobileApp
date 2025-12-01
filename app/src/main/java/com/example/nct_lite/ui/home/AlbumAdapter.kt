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
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.squareup.picasso.Picasso

class AlbumAdapter(
    private val onAlbumClick: (AlbumMetadata) -> Unit
) : ListAdapter<AlbumMetadata, AlbumAdapter.AlbumViewHolder>(AlbumDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album, onAlbumClick)
    }

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image_album)
        private val titleView: TextView = itemView.findViewById(R.id.text_album_title)
        private val artistView: TextView = itemView.findViewById(R.id.text_album_artist)

        fun bind(album: AlbumMetadata, onAlbumClick: (AlbumMetadata) -> Unit) {
            titleView.text = album.title
            artistView.text = album.artist
            if (!album.coverUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(album.coverUrl)
                    .placeholder(R.drawable.placeholder_album)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.placeholder_album)
            }
            itemView.setOnClickListener { onAlbumClick(album) }
        }
    }
}

class AlbumDiffCallback : DiffUtil.ItemCallback<AlbumMetadata>() {
    override fun areItemsTheSame(oldItem: AlbumMetadata, newItem: AlbumMetadata): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: AlbumMetadata, newItem: AlbumMetadata): Boolean {
        return oldItem == newItem
    }
}