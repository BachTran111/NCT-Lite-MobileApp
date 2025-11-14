package com.example.nct_lite.data.remote.model.response

import com.example.nct_lite.data.remote.model.Song

data class SongListResponse(
    val status: String,
    val message: String,
    val metadata: List<Song>
)

data class SongResponse(
    val status: String,
    val message: String,
    val metadata: Song
)
