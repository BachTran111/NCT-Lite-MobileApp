package com.example.nct_lite.data.remote.model

data class Song(
    val _id: String,
    val title: String,
    val artist: String,
    val genreIDs: List<String>,
    val url: String,
    val coverUrl: String?,
    val uploaderId: String?
)
