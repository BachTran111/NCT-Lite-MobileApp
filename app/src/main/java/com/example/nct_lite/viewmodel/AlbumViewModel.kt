package com.example.nct_lite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.remote.model.response.AlbumListResponse
import com.example.nct_lite.data.remote.model.response.AlbumResponse
import com.example.nct_lite.data.repository.AlbumRepository
import kotlinx.coroutines.launch

class AlbumViewModel : ViewModel() {

    private val repo = AlbumRepository()

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
