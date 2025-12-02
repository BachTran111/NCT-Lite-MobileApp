package com.example.nct_lite.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.databinding.ItemPlaylistBinding

class SimplePlaylistAdapter (
    private val onItemClick: (AlbumMetadata) -> Unit
) : RecyclerView.Adapter<SimplePlaylistAdapter.ViewHolder>() {

    private var list = listOf<AlbumMetadata>()

    fun submitList(newList: List<AlbumMetadata>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Tái sử dụng layout item_playlist có sẵn hoặc tạo layout đơn giản mới
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumMetadata) {
            binding.playlistName.text = album.title
            // binding.playlistImage... (Load ảnh nếu cần)

            binding.root.setOnClickListener { onItemClick(album) }
        }
    }
}