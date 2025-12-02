package com.example.nct_lite.data.song.response

data class SongListResponse(
    val status: String,
    val message: String,
    val metadata: List<SongMetadata>
)
