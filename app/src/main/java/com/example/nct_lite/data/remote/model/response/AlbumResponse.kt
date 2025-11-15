package com.example.nct_lite.data.remote.model.response

import com.example.nct_lite.data.remote.model.Album

data class AlbumListResponse(
    val status: String,
    val message: String,
    val metadata: List<Album>
)

data class AlbumResponse(
    val status: String,
    val message: String,
    val metadata: Album
)

