package com.example.nct_lite.data.album.request

data class AlbumCreateRequest(
    val title: String,
    val artist: String? = null,
    val songIDs: List<String> = emptyList(),
    val genreIDs: List<String> = emptyList(),
    val coverUrl: String? = null,
    val releaseDate: String? = null,       // gửi dạng "2025-10-30T00:00:00.000Z" hoặc "2025-10-30"
    val description: String? = null,
    val creatorId: String? = null,
    val isPublic: Boolean = true
)
