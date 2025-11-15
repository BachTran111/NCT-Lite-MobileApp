package com.example.nct_lite.data.repository

import android.util.Log
import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.request.LoginRequest
import com.example.nct_lite.data.remote.model.request.RegisterRequest
import com.example.nct_lite.data.remote.model.response.AuthResponse
import com.example.nct_lite.util.NetworkErrorHandler

class AuthRepository {

    private val api = ApiClient.authApi
    private val tag = "AuthRepository"

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val res = api.login(LoginRequest(username, password))

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                val errorBody = res.errorBody()?.string()
                Log.e(tag, "Login failed: $errorBody")
                Result.failure(Exception(errorBody ?: "Đăng nhập thất bại"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "login", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }

    suspend fun register(username: String, password: String): Result<AuthResponse> {
        return try {
            val res = api.register(RegisterRequest(username, password))

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                val errorBody = res.errorBody()?.string()
                Log.e(tag, "Register failed: $errorBody")
                Result.failure(Exception(errorBody ?: "Đăng ký thất bại"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "register", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }
}
