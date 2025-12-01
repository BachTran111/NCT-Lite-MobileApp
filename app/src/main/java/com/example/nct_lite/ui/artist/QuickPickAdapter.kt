package com.example.nct_lite.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.squareup.picasso.Picasso

class QuickPickAdapter(
    private var songs: List<SongMetadata>,
    private val onSongClicked: (SongMetadata) -> Unit,
    private val onMoreClicked: (SongMetadata) -> Unit
) : RecyclerView.Adapter<QuickPickAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quick_pick, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int = songs.size

    fun updateSongs(newSongs: List<SongMetadata>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvSongTitle)
        private val artist: TextView = itemView.findViewById(R.id.tvArtist)
        private val cover: ImageView = itemView.findViewById(R.id.imgCover)
        private val moreButton: ImageView = itemView.findViewById(R.id.btnMore)

        fun bind(song: SongMetadata) {
            title.text = song.title
            artist.text = song.artist
            Picasso.get()
                .load(song.coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .error(R.drawable.ic_avatar_background)
                .into(cover)
            itemView.setOnClickListener { onSongClicked(song) }
            moreButton.setOnClickListener { onMoreClicked(song) }
        }
    }
}