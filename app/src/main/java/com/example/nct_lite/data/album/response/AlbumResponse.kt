package com.example.nct_lite.data.album.response

import com.example.nct_lite.data.genre.model.Genre

data class AlbumListResponse(
    val status: String,
    val message: String,
    val metadata: List<AlbumMetadata>
)

data class AlbumResponse(
    val status: String,
    val message: String,
    val metadata: AlbumMetadata
)

data class AlbumMetadata(
    val _id: String,
    val title: String,
    val artist: String,
    val songIDs: List<String>,
    val genreIDs: List<Genre>,
    val coverUrl: String,
    val releaseDate: String,
    val description: String?,
    val creatorId: String?,       // null → String? để tránh lỗi parse
    val isPublic: Boolean,
    val createdAt: String,
    val updatedAt: String
    // val __v: Int
)
