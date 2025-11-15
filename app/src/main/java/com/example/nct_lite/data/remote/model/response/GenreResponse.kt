package com.example.nct_lite.data.remote.model.response

import com.example.nct_lite.data.remote.model.Genre

data class GenreListResponse(
    val status: String,
    val message: String,
    val metadata: List<Genre>
)

data class GenreResponse(
    val status: String,
    val message: String,
    val metadata: Genre
)
