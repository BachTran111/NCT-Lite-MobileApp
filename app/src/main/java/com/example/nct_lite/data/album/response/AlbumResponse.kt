package com.example.nct_lite.data.album.response

import android.os.Parcelable
import com.example.nct_lite.data.genre.model.Genre
import java.io.Serializable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

@Parcelize // Tối ưu hóa việc truyền dữ liệu giữa các màn hình (nhanh hơn Serializable)
data class AlbumMetadata(
    @SerializedName("_id")
    val id: String, // Đổi tên biến thành 'id' cho chuẩn Kotlin

    @SerializedName("title")
    val title: String,

    @SerializedName("artist")
    val artist: String?, // Nên để ? vì có thể album không có artist cụ thể

    @SerializedName("songIDs")
    val songIDs: List<String> = emptyList(),

    // LƯU Ý: Chỉ để List<Genre> nếu API có .populate('genreIDs').
    // Nếu API chỉ trả về list ID string ["123", "456"] thì phải sửa thành List<String>
    @SerializedName("genreIDs")
    val genreIDs: List<Genre> = emptyList(),

    @SerializedName("coverUrl")
    val coverUrl: String? = null, // QUAN TRỌNG: Phải cho phép null để tránh crash

    @SerializedName("releaseDate")
    val releaseDate: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("creatorId")
    val creatorId: CreatorId? = null, // Kiểm tra kỹ CreatorId là String hay Object

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
