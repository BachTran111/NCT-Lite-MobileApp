package com.example.nct_lite.viewmodel.genre

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.genre.GenreRepository
import com.example.nct_lite.data.genre.response.GenreListResponse
//import com.example.nct_lite.data.genre.response.GenreResponse
import kotlinx.coroutines.launch

class GenreViewModel(
    private val repo: GenreRepository = GenreRepository()
) : ViewModel() {

    val genres = MutableLiveData<Result<GenreListResponse>>()
//    val genreDetail = MutableLiveData<Result<GenreResponse>>()

    fun getGenres() {
        viewModelScope.launch {
            genres.postValue(repo.getGenres())
        }
    }

//    fun getGenreById(id: String) {
//        viewModelScope.launch {
//            genreDetail.postValue(repo.getGenreById(id))
//        }
//    }
}
