package com.example.nct_lite.data.remote.api

import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HistoryApi {

    @GET("history")
    suspend fun getHistory(): Response<PlayHistoryResponse>

    @POST("history/add")
    suspend fun addToHistory(
        @Body request: Map<String, String> // { "songId": "..." }
    ): Response<PlayHistoryResponse>
}
