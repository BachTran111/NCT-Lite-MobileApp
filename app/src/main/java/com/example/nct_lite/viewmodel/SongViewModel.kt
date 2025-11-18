package com.example.nct_lite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.model.Song
import com.example.nct_lite.data.repository.SongRepository
import kotlinx.coroutines.launch


class SongViewModel : ViewModel() {
    private val repository = SongRepository()
    private val _songs = MutableLiveData<List<Song>>()
    private val _song = MutableLiveData<Song>()

    val songs: LiveData<List<Song>> get() = _songs
    val song: LiveData<Song> get() = _song

    fun loadSongs() {
        repository.fetchAllSongs(_songs)
    }

    fun search(keyword: String) {
        repository.searchSongs(keyword, _songs)
    }

    fun clearSongs() {
        _songs.postValue(emptyList())
    }

    fun refreshSongs() {
        loadSongs()
    }

    private val _songUrl = MutableLiveData<String?>()
    val songUrl: LiveData<String?> get() = _songUrl
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadSong(id: String) {
        viewModelScope.launch {
            if (id.isBlank()) {
                _errorMessage.value = "Song id is empty"
                return@launch
            }

            _isLoading.value = true
            _errorMessage.value = null
            val song = repository.fetchSong(id)
            if (song == null) {
                _errorMessage.value = "Không thể tải bài hát"
                _isLoading.value = false
                return@launch
            }

            _song.value = song
            _songUrl.value = song.audioUrl
            _isLoading.value = false
        }
    }
}
    
