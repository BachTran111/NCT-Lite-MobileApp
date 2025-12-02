package com.example.nct_lite.data.genre

import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.genre.response.GenreListResponse
import com.example.nct_lite.data.genre.response.GenreResponse

class GenreRepository(
    private val remote: GenreRemoteDataSource = GenreRemoteDataSource(ApiClient.genreApi)
) {

    suspend fun getGenres(): Result<GenreListResponse> {
        return try {
            val res = remote.getGenres()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load genres"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun getGenreById(id: String): Result<GenreResponse> {
//        return try {
//            val res = remote.getGenreById(id)
//
//            if (res.isSuccessful && res.body() != null)
//                Result.success(res.body()!!)
//            else
//                Result.failure(Exception("Failed to load genre $id"))
//
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}
