package com.example.nct_lite.data.repository

import android.util.Log
import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.GenreListResponse
import com.example.nct_lite.data.remote.model.response.GenreResponse
import com.example.nct_lite.util.NetworkErrorHandler

class GenreRepository {

    private val api = ApiClient.genreApi
    private val tag = "GenreRepository"

    suspend fun getGenres(): Result<GenreListResponse> {
        return try {
            val res = api.getGenres()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load genres: ${res.code()}")
                Result.failure(Exception("Không thể tải danh sách thể loại"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getGenres", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }

    suspend fun getGenreById(id: String): Result<GenreResponse> {
        return try {
            val res = api.getGenreById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else {
                Log.e(tag, "Failed to load genre $id: ${res.code()}")
                Result.failure(Exception("Không thể tải thông tin thể loại"))
            }

        } catch (e: Exception) {
            NetworkErrorHandler.logError(tag, "getGenreById", e)
            Result.failure(Exception(NetworkErrorHandler.getErrorMessage(e)))
        }
    }
}
