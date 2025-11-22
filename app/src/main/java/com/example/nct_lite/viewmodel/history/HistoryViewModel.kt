package com.example.nct_lite.viewmodel.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.history.HistoryRepository
import com.example.nct_lite.data.history.response.PlayHistoryResponse
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repo: HistoryRepository = HistoryRepository()
) : ViewModel() {

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
