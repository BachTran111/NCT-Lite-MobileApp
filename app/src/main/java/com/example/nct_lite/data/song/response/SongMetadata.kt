package com.example.nct_lite.data.song.response

import com.example.nct_lite.data.genre.model.Genre
import java.io.Serializable


data class SongMetadata(
    val _id: String,
    val title: String,
    val artist: String,
    val genreIDs: List<Genre>,
    val url: String,
    val coverUrl: String?,
    val uploaderId: SongUploader?,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int? = null
): Serializable

data class SongUploader(
    val _id: String,
    val username: String,
    val role: String? = null
): Serializable
