package com.example.nct_lite.data.album

/**
 * Exposes album network calls so higher layers do not talk to [AlbumApi] directly.
 */
class AlbumRemoteDataSource(private val api: AlbumApi) {

    suspend fun getAlbums() = api.getAlbums()

    suspend fun getAlbumById(id: String) = api.getAlbumById(id)
}
