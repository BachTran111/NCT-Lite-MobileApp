package com.example.nct_lite.ui.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata

class AdminPreviewAdapter(
    private val onApprove: (SongMetadata) -> Unit,
    private val onReject: (SongMetadata) -> Unit
) : ListAdapter<SongMetadata, AdminPreviewAdapter.PreviewViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_song, parent, false)
        return PreviewViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PreviewViewHolder(private val root: ViewGroup) : RecyclerView.ViewHolder(root) {
        private val title: TextView = root.findViewById(R.id.tvSongTitle)
        private val artist: TextView = root.findViewById(R.id.tvSongArtist)
        private val genre: TextView = root.findViewById(R.id.tvSongGenre)
        private val approve: ImageView = root.findViewById(R.id.ivApprove)
        private val reject: ImageView = root.findViewById(R.id.ivReject)

        fun bind(song: SongMetadata) {
            title.text = song.title
            artist.text = song.artist
            genre.text = root.context.getString(
                R.string.genre_template,
                song.genreIDs.joinToString { it.name }
            )
            approve.setOnClickListener { onApprove(song) }
            reject.setOnClickListener { onReject(song) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SongMetadata>() {
            override fun areItemsTheSame(oldItem: SongMetadata, newItem: SongMetadata) =
                oldItem._id == newItem._id

            override fun areContentsTheSame(oldItem: SongMetadata, newItem: SongMetadata) =
                oldItem == newItem
        }
    }
}
