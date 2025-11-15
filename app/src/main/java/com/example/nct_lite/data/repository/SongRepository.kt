package com.example.nct_lite.data.repository

import android.util.Log
import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.SongListResponse
import com.example.nct_lite.data.remote.model.response.SongResponse
import com.example.nct_lite.util.NetworkErrorHandler

class SongRepository {

    private val api = ApiClient.songApi
    private val tag = "SongRepository"

    suspend fun getAllSongs(): Result<SongListResponse> {
        return try {
            val res = api.getAllSongs()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load songs: ${res.code()}")
                Result.failure(Exception("Không thể tải danh sách bài hát"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getAllSongs", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }

    suspend fun getSongById(id: String): Result<SongResponse> {
        return try {
            val res = api.getSongById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load song $id: ${res.code()}")
                Result.failure(Exception("Không thể tải thông tin bài hát"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getSongById", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }

    suspend fun searchSongs(keyword: String): Result<SongListResponse> {
        return try {
            val res = api.searchSongs(keyword)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to search songs: ${res.code()}")
                Result.failure(Exception("Không thể tìm kiếm bài hát"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "searchSongs", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }
}
