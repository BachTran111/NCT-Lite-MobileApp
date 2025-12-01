package com.example.nct_lite.viewmodel.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.album.request.AlbumCreateRequest
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse
import kotlinx.coroutines.launch

class AlbumViewModel(
    private val repo: AlbumRepository = AlbumRepository()
) : ViewModel() {

    val albums = MutableLiveData<Result<AlbumListResponse>>()
    val albumDetail = MutableLiveData<Result<AlbumResponse>>()
    val createAlbumResult = MutableLiveData<Result<AlbumResponse>?>()
    private val _myAlbums = MutableLiveData<Result<AlbumListResponse>>()
    val myAlbums: LiveData<Result<AlbumListResponse>> = _myAlbums

    // LiveData cho "Album đã lưu"
    private val _savedAlbums = MutableLiveData<Result<AlbumListResponse>>()
    val savedAlbums: LiveData<Result<AlbumListResponse>> = _savedAlbums
    fun createAlbum(
        albumCreateRequest: AlbumCreateRequest
    ) {
        viewModelScope.launch {
            createAlbumResult.postValue(repo.createAlbum( albumCreateRequest))
        }
    }
    fun getMyOwnAlbum() {
        viewModelScope.launch {
            _myAlbums.postValue(repo.getMyOwnAlbum())
        }
    }
    fun getSavedAlbum(){
        viewModelScope.launch {
            _savedAlbums.postValue(repo.getSavedAlbums())
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
