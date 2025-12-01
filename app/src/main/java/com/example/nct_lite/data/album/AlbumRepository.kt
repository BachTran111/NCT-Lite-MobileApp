package com.example.nct_lite.data.album

import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AlbumRepository(
    private val remote: AlbumRemoteDataSource = AlbumRemoteDataSource(ApiClient.albumApi)
) {
    suspend fun getAlbums(): Result<AlbumListResponse> {
        return try {
            val res = remote.getAlbums()

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to load albums: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumById(id: String): Result<AlbumResponse> {
        return try {
            val res = remote.getAlbumById(id)

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to load album $id: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAlbum(
        token: String,
        title: RequestBody,
        artist: RequestBody,
        genreIDs: RequestBody,
        description: RequestBody,
        isPublic: RequestBody,
        songIDs: RequestBody,
        cover: MultipartBody.Part?
    ): Result<AlbumResponse> {
        return try {
            val res = remote.createAlbum(
                token = token,
                title = title,
                artist = artist,
                genreIDs = genreIDs,
                description = description,
                isPublic = isPublic,
                songIDs = songIDs,
                cover = cover
            )

            if (res.isSuccessful && res.body() != null) {
                Result.success(res.body()!!)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Upload failed: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addSongToAlbum(albumId: String, songId: String): Result<Unit> {
        return try {
            val res = remote.addSongToAlbum(albumId, songId)

            if (res.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to add song $songId to album $albumId: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeSongFromAlbum(albumId: String, songId: String): Result<Unit> {
        return try {
            val res = remote.removeSongFromAlbum(albumId, songId)

            if (res.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to remove song $songId from album $albumId: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveAlbum(albumId: String): Result<Unit> {
        return try {
            val res = remote.saveAlbum(albumId)
            if (res.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to save album $albumId: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unsaveAlbum(albumId: String): Result<Unit> {
        return try {
            val res = remote.unsaveAlbum(albumId)
            if (res.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to unsave album $albumId: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSavedAlbums(): Result<AlbumListResponse> {
        return try {
            val res = remote.getSavedAlbums()
            if (res.isSuccessful) {
                val body = res.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("No saved albums found"))
                }
            } else {
                val errorMsg = res.errorBody()?.string() ?: "Failed to load saved albums: ${res.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
