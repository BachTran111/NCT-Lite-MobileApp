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
}
