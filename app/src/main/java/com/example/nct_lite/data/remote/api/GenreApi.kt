package com.example.nct_lite.data.remote.api

import com.example.nct_lite.data.remote.model.response.GenreListResponse
import com.example.nct_lite.data.remote.model.response.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface   GenreApi {

    @GET("genre")
    suspend fun getGenres(): Response<GenreListResponse>

    @GET("genre/{id}")
    suspend fun getGenreById(
        @Path("id") id: String
    ): Response<GenreResponse>
}
