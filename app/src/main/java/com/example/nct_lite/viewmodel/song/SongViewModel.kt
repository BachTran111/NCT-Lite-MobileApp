package com.example.nct_lite.viewmodel.song

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.data.song.response.SongListResponse
import com.example.nct_lite.data.song.response.SongResponse
import kotlinx.coroutines.launch

class SongViewModel(
    private val repo: SongRepository
) : ViewModel() {

    // LiveData cho HomeFragment (tất cả bài hát)
    private val _songs = MutableLiveData<Result<SongListResponse>>()
    val songs: LiveData<Result<SongListResponse>> get() = _songs

    // LiveData cho chi tiết bài hát
    private val _songDetail = MutableLiveData<Result<SongResponse>>()
    val songDetail: LiveData<Result<SongResponse>> get() = _songDetail

    // LiveData cho kết quả tìm kiếm (dùng trong ArtistPlaylistFragment và SearchFragment)
    private val _searchResult = MutableLiveData<Result<SongListResponse>?>()
    val searchResult: LiveData<Result<SongListResponse>?> get() = _searchResult

    fun loadAllSongs() {
        viewModelScope.launch {
            _songs.postValue(repo.getAllSongs())
        }
    }

    fun getSongById(id: String) {
        viewModelScope.launch {
            _songDetail.postValue(repo.getSongById(id))
        }
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            _searchResult.postValue(repo.searchSongs(keyword))
        }
    }

    fun clearSearchResult() {
        _searchResult.value = null
    }
}
