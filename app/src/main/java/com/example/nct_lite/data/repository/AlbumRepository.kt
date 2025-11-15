package com.example.nct_lite.data.repository

import android.util.Log
import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.AlbumListResponse
import com.example.nct_lite.data.remote.model.response.AlbumResponse
import com.example.nct_lite.util.NetworkErrorHandler

class AlbumRepository {

    private val api = ApiClient.albumApi
    private val tag = "AlbumRepository"

    suspend fun getAlbums(): Result<AlbumListResponse> {
        return try {
            val res = api.getAlbums()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load albums: ${res.code()}")
                Result.failure(Exception("Không thể tải danh sách album"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getAlbums", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }

    suspend fun getAlbumById(id: String): Result<AlbumResponse> {
        return try {
            val res = api.getAlbumById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load album $id: ${res.code()}")
                Result.failure(Exception("Không thể tải thông tin album"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getAlbumById", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }
}
