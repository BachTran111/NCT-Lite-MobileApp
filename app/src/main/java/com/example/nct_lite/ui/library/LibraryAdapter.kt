package com.example.nct_lite.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.squareup.picasso.Picasso

class LibraryAdapter(
    private val onAlbumClick: (AlbumMetadata) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ALBUM = 1
    }

    // Sealed class giúp phân loại dữ liệu: Hoặc là Tiêu đề, Hoặc là Album
    sealed class LibraryItem {
        data class Header(val title: String) : LibraryItem()
        data class Album(val data: AlbumMetadata) : LibraryItem()
    }

    private val items = mutableListOf<LibraryItem>()

    // HÀM QUAN TRỌNG: Nhận 2 danh sách và trộn lại
    fun submitData(myAlbums: List<AlbumMetadata>, savedAlbums: List<AlbumMetadata>) {
        items.clear()

        // 1. Xử lý Album của tôi
        if (myAlbums.isNotEmpty()) {
            items.add(LibraryItem.Header("Playlist của tôi")) // Thêm tiêu đề
            items.addAll(myAlbums.map { LibraryItem.Album(it) }) // Thêm list album
        }

        // 2. Xử lý Album đã lưu
        if (savedAlbums.isNotEmpty()) {
            items.add(LibraryItem.Header("Playlist đã lưu")) // Thêm tiêu đề
            items.addAll(savedAlbums.map { LibraryItem.Album(it) }) // Thêm list album
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is LibraryItem.Header -> TYPE_HEADER
            is LibraryItem.Album -> TYPE_ALBUM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_section_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                // Layout item_playlist bạn đã làm ở bước trước
                val view = inflater.inflate(R.layout.item_playlist, parent, false)
                AlbumViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is LibraryItem.Header -> (holder as HeaderViewHolder).bind(item)
            is LibraryItem.Album -> (holder as AlbumViewHolder).bind(item.data, onAlbumClick)
        }
    }

    override fun getItemCount(): Int = items.size

    // --- VIEW HOLDERS ---

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvHeaderTitle)
        fun bind(item: LibraryItem.Header) {
            title.text = item.title
        }
    }

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cover: ImageView = itemView.findViewById(R.id.playlist_image)
        private val title: TextView = itemView.findViewById(R.id.playlist_name)

        fun bind(album: AlbumMetadata, onClick: (AlbumMetadata) -> Unit) {
            title.text = album.title

            val validCover = if (album.coverUrl.isNullOrEmpty()) null else album.coverUrl
            Picasso.get()
                .load(validCover)
                .placeholder(R.drawable.ic_avatar_background)
                .into(cover)

            itemView.setOnClickListener { onClick(album) }
        }
    }
}