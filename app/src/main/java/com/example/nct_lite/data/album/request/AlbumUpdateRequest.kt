package com.example.nct_lite.data.album.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AlbumUpdateRequest(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("artist")
    val artist: String? = null,
//    @SerializedName("songIDs")
//    val songIDs: List<String>? = null,
    @SerializedName("genreIDs")
    val genreIDs: List<String>? = null,
    @SerializedName("coverUrl")
    val coverUrl: String? = null,
//    @SerializedName("releaseDate")
//    val releaseDate: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("isPublic")
    val isPublic: Boolean? = null
): Serializable