package com.example.nct_lite.data.album

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class AlbumRemoteDataSource(private val albumApi: AlbumApi) {

    suspend fun getAlbums() = albumApi.getAlbums()

    suspend fun getAlbumById(id: String) = albumApi.getAlbumById(id)

    // Trong Repository của bạn
    suspend fun createAlbum(
        token: String,
        title: RequestBody,
        artist: RequestBody,
        genreIDs: RequestBody,
        description: RequestBody,
        isPublic: RequestBody,
        songIDs: RequestBody,
        cover: MultipartBody.Part?
    ) = albumApi.createAlbum(
        token = token,
        title = title,
        artist = artist,
        description = description,
        isPublic = isPublic,
        genreIDs = genreIDs, // Đã thêm vào
        songIDs = songIDs,
        cover = cover
    )

    suspend fun addSongToAlbum(albumId: String, songId: String) =
        albumApi.addSongToAlbum(albumId, songId)

    suspend fun removeSongFromAlbum(albumId: String, songId: String) =
        albumApi.removeSongFromAlbum(albumId, songId)

    suspend fun saveAlbum(albumId: String) = albumApi.saveAlbum(albumId)

    suspend fun unsaveAlbum(albumId: String) = albumApi.unsaveAlbum(albumId)

    suspend fun getSavedAlbums() = albumApi.getSavedAlbums()
}