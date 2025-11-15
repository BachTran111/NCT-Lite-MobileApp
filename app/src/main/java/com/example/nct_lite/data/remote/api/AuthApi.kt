package com.example.nct_lite.data.remote.api

import com.example.nct_lite.data.remote.model.request.LoginRequest
import com.example.nct_lite.data.remote.model.request.RegisterRequest
import com.example.nct_lite.data.remote.model.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}
