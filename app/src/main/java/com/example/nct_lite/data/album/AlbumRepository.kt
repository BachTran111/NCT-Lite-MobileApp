package com.example.nct_lite.data.album

import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse

class AlbumRepository(
    private val remote: AlbumRemoteDataSource = AlbumRemoteDataSource(ApiClient.albumApi)
) {
    suspend fun getAlbums(): Result<AlbumListResponse> {
        return try {
            val res = remote.getAlbums()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load albums"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumById(id: String): Result<AlbumResponse> {
        return try {
            val res = remote.getAlbumById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load album $id"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun addSongToAlbum(albumId: String, songId: String): Result<Unit> {
        return try {
            val res = remote.addSongToAlbum(albumId, songId)

            if (res.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception("Failed to add song $songId to album $albumId"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun removeSongFromAlbum(albumId: String, songId: String): Result<Unit> {
        return try {
            val res = remote.removeSongFromAlbum(albumId, songId)

            if (res.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception("Failed to remove song $songId from album $albumId"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun saveAlbum(albumId: String): Result<Unit> {
        return try {
            val res = remote.saveAlbum(albumId)
            if (res.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception("Failed to save album $albumId"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun unsaveAlbum(albumId: String): Result<Unit> {
        return try {
            val res = remote.unsaveAlbum(albumId)
            if (res.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception("Failed to unsave album $albumId"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getSavedAlbums(): Result<AlbumListResponse> {
        return try {
            val res = remote.getSavedAlbums()
            if (res.isSuccessful){
                val body = res.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("No saved albums found"))
                }
            } else {
                Result.failure(Exception("Failed to load saved albums"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
