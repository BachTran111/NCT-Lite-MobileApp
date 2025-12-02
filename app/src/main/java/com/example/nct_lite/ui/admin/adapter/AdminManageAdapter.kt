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

class AdminManageAdapter(
    private val onPreview: (SongMetadata) -> Unit,
    private val onEdit: (SongMetadata) -> Unit,
    private val onReject: (SongMetadata) -> Unit
) : ListAdapter<SongMetadata, AdminManageAdapter.ManageViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_song_1, parent, false)
        return ManageViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ManageViewHolder(private val root: ViewGroup) : RecyclerView.ViewHolder(root) {
        private val title: TextView = root.findViewById(R.id.tvSongTitle)
        private val artist: TextView = root.findViewById(R.id.tvSongArtist)
        private val genre: TextView = root.findViewById(R.id.tvSongGenre)
        private val preview: ImageView = root.findViewById(R.id.ivReplay)
        private val edit: ImageView = root.findViewById(R.id.ivEdit)
        private val delete: ImageView = root.findViewById(R.id.ivDelete)

        fun bind(song: SongMetadata) {
            title.text = song.title
            artist.text = song.artist
            genre.text = root.context.getString(
                R.string.genre_template,
                song.genreIDs.joinToString { it.name }
            )
            preview.setOnClickListener { onPreview(song) }
            edit.setOnClickListener { onEdit(song) }
            delete.setOnClickListener { onReject(song) }
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
