package com.example.nct_lite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nct_lite.data.model.Playlist

class PlaylistViewModel : ViewModel() {

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        loadPlaylists()
    }

    // Giả lập load dữ liệu từ database hoặc API
    private fun loadPlaylists() {
        try {
            val sampleData = listOf(
                Playlist("Shazam", "https://example.com/cover1.png"),
                Playlist("Top Hits", "https://example.com/cover2.png"),
                Playlist("Favorites", "https://example.com/cover3.png")
            )
            _playlists.value = sampleData
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}
