package com.example.nct_lite.data.auth

import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.auth.request.RegisterRequest
import com.example.nct_lite.data.auth.request.LoginRequest
import com.example.nct_lite.data.auth.response.AuthResponse
import com.example.nct_lite.data.auth.response.InforResponse
//    private val local: AuthLocalDataSource,

class AuthRepository (
    private val api: AuthRemoteDataSource
    ) {

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val res = api.login(username, password)

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
            val res = api.register(username, password)

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception(res.errorBody()?.string() ?: "Register failed"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getInfor(): Result<InforResponse> {
        return try {
            val res = api.getInfor()

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                Result.failure(Exception(res.errorBody()?.string() ?: "Get infor"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}