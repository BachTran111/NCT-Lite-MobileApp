package com.example.nct_lite.viewmodel.song

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.data.song.SongRemoteDataSource
import com.example.nct_lite.viewmodel.album.AlbumViewModel


class SongViewModelFactory(
    private val repo: SongRepository = SongRepository()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
