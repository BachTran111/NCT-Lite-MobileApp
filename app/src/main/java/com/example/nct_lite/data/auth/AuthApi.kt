package com.example.nct_lite.data.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.nct_lite.data.auth.response.AuthResponse;
import com.example.nct_lite.data.auth.request.LoginRequest;
import com.example.nct_lite.data.auth.request.RegisterRequest;
import com.example.nct_lite.data.auth.response.InforResponse
import retrofit2.http.GET


interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getInfor(): Response<InforResponse>
}