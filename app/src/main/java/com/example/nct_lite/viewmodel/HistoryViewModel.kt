package com.example.nct_lite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse
import com.example.nct_lite.data.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repo = HistoryRepository()

    val history = MutableLiveData<Result<PlayHistoryResponse>>()
    val addHistoryResult = MutableLiveData<Result<PlayHistoryResponse>>()

    fun getHistory() {
        viewModelScope.launch {
            history.postValue(repo.getHistory())
        }
    }

    fun addToHistory(songId: String) {
        viewModelScope.launch {
            addHistoryResult.postValue(repo.addToHistory(songId))
        }
    }
}
