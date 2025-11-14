package com.example.nct_lite.data.remote.model.response

data class AuthMeta(
    val token: String,
    val role: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val metadata: AuthMeta
)
