package com.example.nct_lite.data.auth.response

data class AuthMeta(
    val token: String,
    val role: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val metadata: AuthMeta
)

data class InforResponse(
    val _id: String,
    val username: String,
    val role: String
)
