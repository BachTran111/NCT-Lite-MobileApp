package com.example.nct_lite.data.auth.request

class RegisterRequest (
    val username: String,
    val password: String,
    val role: String = "USER"
    )