package com.example.nct_lite.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.squareup.picasso.Picasso

class AlbumAdapter(
    // Chuyển sang MutableList để có thể thay đổi danh sách
    private var albums: MutableList<AlbumMetadata>,
    private val onItemClicked: (AlbumMetadata) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album, onItemClicked)
    }

    override fun getItemCount(): Int = albums.size

    // --- CÁC PHƯƠNG THỨC CRUD ---

    // Create: Thêm một album mới vào danh sách
    fun addAlbum(album: AlbumMetadata) {
        albums.add(album)
        notifyItemInserted(albums.size - 1)
    }

    // Read: Lấy album tại vị trí cụ thể (có sẵn qua `albums[position]`)

    // Update: Cập nhật toàn bộ danh sách
    fun updateList(newAlbums: List<AlbumMetadata>) {
        albums.clear()
        albums.addAll(newAlbums)
        notifyDataSetChanged()
    }

    // Update: Cập nhật một album tại vị trí cụ thể
    fun updateAlbum(position: Int, updatedAlbum: AlbumMetadata) {
        if (position >= 0 && position < albums.size) {
            albums[position] = updatedAlbum
            notifyItemChanged(position)
        }
    }

    // Delete: Xóa một album khỏi danh sách
    fun removeAlbum(position: Int) {
        if (position >= 0 && position < albums.size) {
            albums.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.playlist_image)
        private val title: TextView = itemView.findViewById(R.id.playlist_name)
        private val info: TextView = itemView.findViewById(R.id.playlist_info)

        fun bind(album: AlbumMetadata, onItemClicked: (AlbumMetadata) -> Unit) {
            title.text = album.title
            info.text = "Album • ${album.artist}"
            Picasso.get()
                .load(album.coverUrl)
                .placeholder(R.drawable.placeholder_album)
                .into(cover)

            itemView.setOnClickListener { onItemClicked(album) }
        }
    }
}
