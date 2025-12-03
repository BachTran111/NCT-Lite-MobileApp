package com.example.nct_lite.viewmodel.album

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.album.request.AlbumCreateRequest
import com.example.nct_lite.data.album.request.AlbumUpdateRequest
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse
import com.example.nct_lite.data.album.response.AlbumSongsResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AlbumViewModel(
    private val repo: AlbumRepository = AlbumRepository()
) : ViewModel() {
    private val _addSongResult = MutableLiveData<Result<Unit>?>()
    val addSongResult: LiveData<Result<Unit>?> = _addSongResult
    private val _removeSongResult = MutableLiveData<Result<Unit>?>()
    val removeSongResult: LiveData<Result<Unit>?> = _removeSongResult
    private val _updateAlbumResult = MutableLiveData<Result<AlbumResponse>?>()
    val updateAlbumResult: LiveData<Result<AlbumResponse>?> = _updateAlbumResult
    val _deleteAlbumResult = MutableLiveData<Result<Unit>?>()
    val deleteAlbumResult: LiveData<Result<Unit>?> = _deleteAlbumResult

    val _resetAddSongResult = MutableLiveData< Result<Unit>?>()
    val resetAddSongResult: LiveData<Result<Unit>?> = _resetAddSongResult
    val albums = MutableLiveData<Result<AlbumListResponse>>()
    val albumDetail = MutableLiveData<Result<AlbumSongsResponse>>()
    val createAlbumResult = MutableLiveData<Result<AlbumResponse>?>()
    private val _myAlbums = MutableLiveData<Result<AlbumListResponse>>()
    val myAlbums: LiveData<Result<AlbumListResponse>> = _myAlbums
    private val _publicAlbums = MutableLiveData<Result<AlbumListResponse>>()
    val publicAlbums: LiveData<Result<AlbumListResponse>> = _publicAlbums
    private val _savedAlbums = MutableLiveData<Result<AlbumListResponse>>()
    val savedAlbums: LiveData<Result<AlbumListResponse>> = _savedAlbums
    private val _saveAlbumResult = MutableLiveData<Result<Boolean>?>()
    val saveAlbumResult: LiveData<Result<Boolean>?> = _saveAlbumResult

    fun save(albumId: String) {
        viewModelScope.launch {
            try {
                val result = repo.saveAlbum(albumId)
                if (result.isSuccess) {
                    _saveAlbumResult.postValue(Result.success(true)) // true = saved
                } else {
                    _saveAlbumResult.postValue(Result.failure(result.exceptionOrNull() ?: Exception("Save failed")))
                }
            } catch (e: Exception) {
                _saveAlbumResult.postValue(Result.failure(e))
            }
        }
    }
    fun createAlbum(
        title: RequestBody,
        description: RequestBody?,
        isPublic: RequestBody?,
        cover: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            createAlbumResult.postValue(repo.createAlbum( title, description, isPublic, cover))
        }
    }
    fun unsave(albumId: String) {
        viewModelScope.launch {
            try {
                val result = repo.unsaveAlbum(albumId)
                if (result.isSuccess) {
                    _saveAlbumResult.postValue(Result.success(false))
                    getSavedAlbums()
                } else {
                    _saveAlbumResult.postValue(Result.failure(result.exceptionOrNull() ?: Exception("Unsave failed")))
                }
            } catch (e: Exception) {
                _saveAlbumResult.postValue(Result.failure(e))
            }
        }
    }
    fun updateAlbum(
        albumId: String,
        title: String,
        artist: String,
        description: String,
        coverUrl: String,
        isPublic: Boolean
    ) {
        viewModelScope.launch {
            val request = AlbumUpdateRequest(
                title = title,
                artist = artist,
                description = description,
                coverUrl = coverUrl,
                isPublic = isPublic
            )

            try {
                val result = repo.updateAlbum(albumId, request)
                _updateAlbumResult.postValue(result)
            } catch (e: Exception) {
                _updateAlbumResult.postValue(Result.failure(e))
            }
        }
    }
    fun deleteAlbum(albumId: String) {
        viewModelScope.launch {
            val result = repo.deleteAlbum(albumId)
            _deleteAlbumResult.postValue(result)
        }
    }
    fun getMyOwnAlbum() {
        viewModelScope.launch {
            _myAlbums.postValue(repo.getMyOwnAlbum())
        }
    }
    fun getSavedAlbums() {
        viewModelScope.launch {
            try {
                val response = repo.getSavedAlbums()
                _savedAlbums.postValue(response)
            } catch (e: Exception) {
                _savedAlbums.postValue(Result.failure(e))
            }
        }}

    fun getAllAlbums() {
        viewModelScope.launch {
            _publicAlbums.postValue(repo.getAlbums())
        }
    }

    fun getAlbumById(id: String) {
        viewModelScope.launch {
            albumDetail.postValue(repo.getAlbumById(id))
        }
    }
    fun addSongToAlbum(albumId: String, songId: String){
        viewModelScope.launch {
            val result = repo.addSongToAlbum(albumId, songId)
            _addSongResult.postValue(result)
        }
    }
    fun moveSongFromAlbum(albumId: String, songId: String){
        viewModelScope.launch {
            val result = repo.removeSongFromAlbum(albumId, songId)
            _removeSongResult.postValue(result)

        }
    }
    fun resetStatus() {
        _addSongResult.value = null
        _removeSongResult.value = null
        _updateAlbumResult.value = null
    }
    fun resetAddSongResult(){
        _addSongResult.value = null
    }
}

