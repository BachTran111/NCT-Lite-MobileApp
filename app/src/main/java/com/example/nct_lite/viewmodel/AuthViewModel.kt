package com.example.nct_lite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.AuthResponse
import com.example.nct_lite.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    val authResponse = MutableLiveData<Result<AuthResponse>>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(username, password)

            // Nếu login thành công → Lưu token vào ApiClient
            result.onSuccess {
                ApiClient.authToken = it.metadata.token
            }

            authResponse.postValue(result)
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val result = repo.register(username, password)
            authResponse.postValue(result)
        }
    }
}
