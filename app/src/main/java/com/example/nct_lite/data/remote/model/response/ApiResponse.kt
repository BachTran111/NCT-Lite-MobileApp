package com.example.nct_lite.data.remote.model.response

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val metadata: T?
)
