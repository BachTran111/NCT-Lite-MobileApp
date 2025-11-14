package com.example.nct_lite.data.repository

import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.request.LoginRequest
import com.example.nct_lite.data.remote.model.request.RegisterRequest
import com.example.nct_lite.data.remote.model.response.AuthResponse

class AuthRepository {

    private val api = ApiClient.authApi

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val res = api.login(LoginRequest(username, password))

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception(res.errorBody()?.string() ?: "Login failed"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String): Result<AuthResponse> {
        return try {
            val res = api.register(RegisterRequest(username, password))

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception(res.errorBody()?.string() ?: "Register failed"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
