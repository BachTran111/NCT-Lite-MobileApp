package com.example.nct_lite.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.squareup.picasso.Picasso

class ItemPlaylistSongAdapter(
    private val songs: List<SongMetadata>,
    private val onClick: (SongMetadata) -> Unit,
    private val onMoreClick: (SongMetadata, View) -> Unit
) : RecyclerView.Adapter<ItemPlaylistSongAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.imgCover)
        val title: TextView = view.findViewById(R.id.tvSongTitle)
        val artist: TextView = view.findViewById(R.id.tvArtist)
        val btnMore: ImageView = view.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title
        holder.artist.text = song.artist ?: "Unknown"

        val validCover = if (song.coverUrl.isNullOrEmpty()) null else song.coverUrl
        Picasso.get()
            .load(validCover)
            .placeholder(R.drawable.ic_avatar_background)
            .error(R.drawable.ic_avatar_background)
            .into(holder.cover)

        holder.itemView.setOnClickListener { onClick(song) }

        holder.btnMore.setOnClickListener { onMoreClick(song, it) }
    }

    override fun getItemCount() = songs.size
}