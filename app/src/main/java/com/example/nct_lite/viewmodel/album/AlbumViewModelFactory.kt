package com.example.nct_lite.viewmodel.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.album.AlbumRemoteDataSource

class AlbumViewModelFactory(
    private val repo: AlbumRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            // Nếu repo không được cung cấp, hãy tạo một repo mới với ApiClient đã được cấu hình
            val albumRepository = repo ?: AlbumRepository(AlbumRemoteDataSource(ApiClient.albumApi))
            return AlbumViewModel(albumRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
