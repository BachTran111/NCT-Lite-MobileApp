package com.example.nct_lite.data.album.response

import com.example.nct_lite.data.genre.model.Genre
import java.io.Serializable

data class AlbumListResponse(
    val status: String,
    val message: String,
    val metadata: List<AlbumMetadata>
): Serializable


data class AlbumResponse(
    val status: String,
    val message: String,
    val metadata: AlbumMetadata
): Serializable

data class AlbumMetadata(
    val _id: String,
    val title: String,
    val artist: String,
    val songIDs: List<String>,
    val genreIDs: List<Genre>,
    val coverUrl: String,
    val releaseDate: String?,
    val description: String?,
    val creatorId: CreatorId?,
    val isPublic: Boolean,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val __v: Int? = null
): Serializable


data class CreatorId(
    val _id: String,
    val username: String
): Serializable
