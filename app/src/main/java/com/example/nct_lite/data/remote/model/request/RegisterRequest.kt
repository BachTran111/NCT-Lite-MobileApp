package com.example.nct_lite.data.remote.model.request

data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String = "USER"
)
