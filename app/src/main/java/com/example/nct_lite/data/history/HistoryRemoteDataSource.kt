package com.example.nct_lite.data.history

/**
 * Remote data source for play history features.
 */
class HistoryRemoteDataSource(private val api: HistoryApi) {

    suspend fun getHistory() = api.getHistory()

    suspend fun addToHistory(songId: String) = api.addToHistory(mapOf("songId" to songId))
}
