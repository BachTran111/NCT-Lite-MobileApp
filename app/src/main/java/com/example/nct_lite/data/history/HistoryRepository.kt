package com.example.nct_lite.data.history

import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.history.response.PlayHistoryResponse

class HistoryRepository(
    private val remote: HistoryRemoteDataSource = HistoryRemoteDataSource(ApiClient.historyApi)
) {

    suspend fun getHistory(): Result<PlayHistoryResponse> {
        return try {
            val res = remote.getHistory()

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
            val res = remote.addToHistory(songId)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to add history"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
