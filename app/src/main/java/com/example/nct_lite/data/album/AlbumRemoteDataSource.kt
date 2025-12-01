package com.example.nct_lite.data.album

import com.example.nct_lite.data.album.request.AlbumCreateRequest

class AlbumRemoteDataSource(private val albumApi: AlbumApi) {

    suspend fun getAlbums() = albumApi.getAlbums()

    suspend fun getAlbumById(id: String) = albumApi.getAlbumById(id)

    suspend fun createAlbum(
        albumCreateRequest: AlbumCreateRequest
    ) = albumApi.createAlbum(
        albumCreateRequest = albumCreateRequest
    )
    suspend fun getMyOwnAlbum() = albumApi.getMyOwnALbum()
    suspend fun addSongToAlbum(albumId: String, songId: String) =
        albumApi.addSongToAlbum(albumId, songId)
    suspend fun removeSongFromAlbum(albumId: String, songId: String) =
        albumApi.removeSongFromAlbum(albumId, songId)
    suspend fun saveAlbum(albumId: String) = albumApi.saveAlbum(albumId)
    suspend fun unsaveAlbum(albumId: String) = albumApi.unsaveAlbum(albumId)
    suspend fun getSavedAlbums() = albumApi.getSavedAlbums()
}
