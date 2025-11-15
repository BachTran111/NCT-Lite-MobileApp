package com.example.nct_lite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.remote.model.response.GenreListResponse
import com.example.nct_lite.data.remote.model.response.GenreResponse
import com.example.nct_lite.data.repository.GenreRepository
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel() {

    private val repo = GenreRepository()

    val genres = MutableLiveData<Result<GenreListResponse>>()
    val genreDetail = MutableLiveData<Result<GenreResponse>>()

    fun getGenres() {
        viewModelScope.launch {
            genres.postValue(repo.getGenres())
        }
    }

    fun getGenreById(id: String) {
        viewModelScope.launch {
            genreDetail.postValue(repo.getGenreById(id))
        }
    }
}
