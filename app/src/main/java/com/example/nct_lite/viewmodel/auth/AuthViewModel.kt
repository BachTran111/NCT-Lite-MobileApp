package com.example.nct_lite.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.auth.AuthRepository
import com.example.nct_lite.data.auth.response.AuthResponse
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _state = MutableLiveData<String>()
    val state: LiveData<String> = _state

    val authResponse = MutableLiveData<Result<AuthResponse>>()
    val userRole = MutableLiveData<String>()
    val username = MutableLiveData<String>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(username, password)

            // Nếu login thành công → Lưu token vào ApiClient
            result.onSuccess {
                ApiClient.authToken = it.metadata.token
                userRole.postValue(it.metadata.role)
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
    fun getInfor() {
        viewModelScope.launch {
            val result = repo.getInfor()
            result.onSuccess {
                userRole.postValue(it.role)
                username.postValue(it.username)
            }
        }
    }
}
