package com.example.nct_lite.data.repository

import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse

class HistoryRepository {

    private val api = ApiClient.historyApi

    suspend fun getHistory(): Result<PlayHistoryResponse> {
        return try {
            val res = api.getHistory()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load play history"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToHistory(songId: String): Result<PlayHistoryResponse> {
        return try {
            val res = api.addToHistory(mapOf("songId" to songId))

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to add history"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
