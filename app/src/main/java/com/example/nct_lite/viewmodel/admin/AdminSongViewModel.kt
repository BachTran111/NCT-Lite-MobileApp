package com.example.nct_lite.viewmodel.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.data.song.response.SongListResponse
import com.example.nct_lite.data.song.response.SongResponse
import kotlinx.coroutines.launch

class AdminSongViewModel(
    private val repository: SongRepository = SongRepository()
) : ViewModel() {

    val pendingSongs = MutableLiveData<Result<SongListResponse>>()
    val actionResult = MutableLiveData<Result<SongResponse>>()

    fun loadPendingSongs() {
        viewModelScope.launch {
            pendingSongs.postValue(repository.getPendingSongs())
        }
    }

    fun approveSong(id: String) {
        viewModelScope.launch {
            actionResult.postValue(repository.approveSong(id))
            loadPendingSongs()
        }
    }

    fun rejectSong(id: String) {
        viewModelScope.launch {
            actionResult.postValue(repository.rejectSong(id))
            loadPendingSongs()
        }
    }
}
