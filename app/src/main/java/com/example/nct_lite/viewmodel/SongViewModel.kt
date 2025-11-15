package com.example.nct_lite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.remote.model.response.SongListResponse
import com.example.nct_lite.data.remote.model.response.SongResponse
import com.example.nct_lite.data.repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {

    private val repo = SongRepository()

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
