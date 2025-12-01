package com.example.nct_lite.data.album.request

import java.io.Serializable


data class AlbumUpdateRequest(
    val title: String? = null,
    val artist: String? = null,
    val songIDs: List<String>? = null,
    val genreIDs: List<String>? = null,
    val coverUrl: String? = null,
    val releaseDate: String? = null,
    val description: String? = null,
    val isPublic: Boolean? = null
): Serializable
