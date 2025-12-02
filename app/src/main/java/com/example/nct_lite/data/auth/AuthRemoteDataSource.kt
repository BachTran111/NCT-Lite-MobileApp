package com.example.nct_lite.data.auth

import com.example.nct_lite.data.auth.request.LoginRequest
import com.example.nct_lite.data.auth.request.RegisterRequest

class AuthRemoteDataSource (private val api: AuthApi) {
    suspend fun login(username: String, password: String) =
        api.login(LoginRequest(username, password))
    suspend fun register(username: String, password: String) =
        api.register(RegisterRequest(username, password))
}