package com.example.nct_lite.viewmodel.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val repo: AlbumRepository = AlbumRepository()
) : ViewModel() {

    val albums = MutableLiveData<Result<AlbumListResponse>>()
    val albumDetail = MutableLiveData<Result<AlbumResponse>>()

    fun getAllAlbums() {
        viewModelScope.launch {
            albums.postValue(repo.getAlbums())
        }
    }

    fun getAlbumById(id: String) {
        viewModelScope.launch {
            albumDetail.postValue(repo.getAlbumById(id))
        }
    }
}
