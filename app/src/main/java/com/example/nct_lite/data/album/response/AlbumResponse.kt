package com.example.nct_lite.data.album.response

import android.os.Parcelable
import com.example.nct_lite.data.genre.model.Genre
import com.example.nct_lite.data.song.response.SongMetadata
import java.io.Serializable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

//import kotlinx.android.parcel.Parcelize

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

//data class AlbumMetadata(
//    val _id: String,
//    val title: String,
//    val artist: String,
//    val songIDs: List<String>,
//    val genreIDs: List<Genre>,
//    val coverUrl: String,
//    val releaseDate: String?,
//    val description: String?,
//    val creatorId: CreatorId?,
//    val isPublic: Boolean,
//    val createdAt: String? = null,
//    val updatedAt: String? = null,
//    val __v: Int? = null
//): Serializable

@Parcelize
data class AlbumMetadata(
    @SerializedName("_id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("artist")
    val artist: String?,

    @SerializedName("songIDs")
    val songIDs: @RawValue List<Any>? = null,
//    val songIDs: List<SongMetadata> = emptyList(),


    @SerializedName("genreIDs")
    val genreIDs: List<Genre> = emptyList(),

    @SerializedName("coverUrl")
    val coverUrl: String? = null,

    @SerializedName("releaseDate")
    val releaseDate: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("creatorId")
    val creatorId: @RawValue Any? = null,

    @SerializedName("isPublic")
    val isPublic: Boolean = true,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("__v")
    val v: Int? = null
) : Parcelable

data class CreatorId(
    val _id: String,
    val username: String
): Serializable

data class AlbumSongsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("metadata") val metadata: AlbumSongsData // Object chứa album và songs
)

data class AlbumSongsData(
    @SerializedName("album") val album: AlbumMetadata,
    @SerializedName("songs") val songs: List<SongMetadata>
)
