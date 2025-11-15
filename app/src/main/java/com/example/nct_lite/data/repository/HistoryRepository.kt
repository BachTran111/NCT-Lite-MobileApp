package com.example.nct_lite.data.repository

import android.util.Log
import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse
import com.example.nct_lite.util.NetworkErrorHandler

class HistoryRepository {

    private val api = ApiClient.historyApi
    private val tag = "HistoryRepository"

    suspend fun getHistory(): Result<PlayHistoryResponse> {
        return try {
            val res = api.getHistory()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load play history: ${res.code()}")
                Result.failure(Exception("Không thể tải lịch sử phát"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getHistory", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }

    suspend fun addToHistory(songId: String): Result<PlayHistoryResponse> {
        return try {
            val res = api.addToHistory(mapOf("songId" to songId))

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to add history: ${res.code()}")
                Result.failure(Exception("Không thể thêm vào lịch sử"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "addToHistory", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }
}
