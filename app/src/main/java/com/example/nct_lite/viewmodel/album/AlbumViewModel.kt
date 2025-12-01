package com.example.nct_lite.viewmodel.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AlbumViewModel(
    private val repo: AlbumRepository = AlbumRepository()
) : ViewModel() {

    val albums = MutableLiveData<Result<AlbumListResponse>>()
    val albumDetail = MutableLiveData<Result<AlbumResponse>>()
    val createAlbumResult = MutableLiveData<Result<AlbumResponse>>()
    fun createAlbum(
        token: String,
        title: RequestBody,
        artist: RequestBody,
        genreIDs: RequestBody,
        description: RequestBody,
        isPublic: RequestBody,
        songIDs: RequestBody,
        cover: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            createAlbumResult.postValue(repo.createAlbum(token, title, artist, genreIDs, description, isPublic, songIDs, cover))
        }
    }

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
