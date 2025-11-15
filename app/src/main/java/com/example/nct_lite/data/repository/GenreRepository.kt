package com.example.nct_lite.data.repository

import com.example.nct_lite.data.remote.ApiClient
import com.example.nct_lite.data.remote.model.response.GenreListResponse
import com.example.nct_lite.data.remote.model.response.GenreResponse

class GenreRepository {

    private val api = ApiClient.genreApi

    suspend fun getGenres(): Result<GenreListResponse> {
        return try {
            val res = api.getGenres()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load genres"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGenreById(id: String): Result<GenreResponse> {
        return try {
            val res = api.getGenreById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load genre $id"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
