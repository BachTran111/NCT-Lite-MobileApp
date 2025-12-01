package com.example.nct_lite.data.album.request

import com.google.gson.annotations.SerializedName

data class AlbumCreateRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("artist")
    val artist: String? = null,
    @SerializedName("isPublic")
    val isPublic: Boolean? = true,
    @SerializedName("releaseDate")
    val releaseDate: String? = "",
    @SerializedName("coverUrl")
    val coverUrl: String? = "",
    @SerializedName("songIDs")
    val songIDs: List<String>? = listOf(),
    @SerializedName("genreIDs")
    val genreIDs: List<String>? = listOf()
)
