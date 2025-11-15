package com.example.nct_lite.data.repository

import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.SongListResponse
import com.example.nct_lite.data.remote.model.response.SongResponse

class SongRepository {

    private val api = ApiClient.songApi

    suspend fun getAllSongs(): Result<SongListResponse> {
        return try {
            val res = api.getAllSongs()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load songs"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSongById(id: String): Result<SongResponse> {
        return try {
            val res = api.getSongById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load song $id"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchSongs(keyword: String): Result<SongListResponse> {
        return try {
            val res = api.searchSongs(keyword)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to search songs"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
