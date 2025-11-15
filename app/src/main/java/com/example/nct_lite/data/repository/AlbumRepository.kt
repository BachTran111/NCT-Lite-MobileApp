package com.example.nct_lite.data.repository

import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.AlbumListResponse
import com.example.nct_lite.data.remote.model.response.AlbumResponse

class AlbumRepository {

    private val api = ApiClient.albumApi

    suspend fun getAlbums(): Result<AlbumListResponse> {
        return try {
            val res = api.getAlbums()

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
            val res = api.getAlbumById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load album $id"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
