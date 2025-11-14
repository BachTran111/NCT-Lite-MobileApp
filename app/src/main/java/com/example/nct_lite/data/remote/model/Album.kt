package com.example.nct_lite.data.remote.model

data class Album(
    val _id: String,
    val title: String,
    val artist: String?,
    val songIDs: List<String>,
    val genreIDs: List<String>,
    val coverUrl: String?,
    val releaseDate: String?,
    val description: String?,
    val creatorId: String?,
    val isPublic: Boolean
)
