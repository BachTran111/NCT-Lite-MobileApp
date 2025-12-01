package com.example.nct_lite.data.album
class AlbumRemoteDataSource(private val api: AlbumApi) {

    suspend fun getAlbums() = api.getAlbums()

    suspend fun getAlbumById(id: String) = api.getAlbumById(id)
    suspend fun addSongToAlbum(albumId: String, songId: String) =
        api.addSongToAlbum(albumId, songId)
    suspend fun removeSongFromAlbum(albumId: String, songId: String) =
        api.removeSongFromAlbum(albumId, songId)
    suspend fun saveAlbum(albumId: String) = api.saveAlbum(albumId)
    suspend fun unsaveAlbum(albumId: String) = api.unsaveAlbum(albumId)
    suspend fun getSavedAlbums() = api.getSavedAlbums()
}
