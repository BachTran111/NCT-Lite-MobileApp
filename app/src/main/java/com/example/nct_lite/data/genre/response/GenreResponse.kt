package com.example.nct_lite.data.genre.response

import com.example.nct_lite.data.genre.model.Genre

data class GenreListResponse(
    val status: String,
    val message: String,
    val metadata: List<Genre>
)

// data class GenreResponse(
//     val status: String,
//     val message: String,
//     val metadata: Genre
// )
