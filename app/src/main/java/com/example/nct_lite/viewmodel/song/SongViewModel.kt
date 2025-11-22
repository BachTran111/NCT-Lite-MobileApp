package com.example.nct_lite.viewmodel.song

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.data.song.response.SongListResponse
import com.example.nct_lite.data.song.response.SongResponse
import kotlinx.coroutines.launch

class SongViewModel(
    private val repo: SongRepository = SongRepository()
) : ViewModel() {

    val songs = MutableLiveData<Result<SongListResponse>>()
    val songDetail = MutableLiveData<Result<SongResponse>>()
    val searchResult = MutableLiveData<Result<SongListResponse>>()

    fun loadAllSongs() {
        viewModelScope.launch {
            songs.postValue(repo.getAllSongs())
        }
    }

    fun getSongById(id: String) {
        viewModelScope.launch {
            songDetail.postValue(repo.getSongById(id))
        }
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            searchResult.postValue(repo.searchSongs(keyword))
        }
    }
}
