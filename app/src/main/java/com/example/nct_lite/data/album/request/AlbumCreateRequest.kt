package com.example.nct_lite.data.album.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AlbumCreateRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("artist")
    val artist: String? = null,

    @SerializedName("songIDs")
    val songIDs: List<String> = emptyList(),

    @SerializedName("genreIDs")
    val genreIDs: List<String> = emptyList(),

    @SerializedName("coverUrl")
    val coverUrl: String? = null,

    @SerializedName("releaseDate")
    val releaseDate: String? = null,

    @SerializedName("isPublic")
    val isPublic: Boolean = true
): Serializable
