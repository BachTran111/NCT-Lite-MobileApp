package com.example.nct_lite.data.song.request

data class SongUpdateRequest(
    val title: String? = null,
    val artist: String? = null,
    val genreIDs: List<String>? = null,
    val url: String? = null,
    val coverUrl: String? = null
)
