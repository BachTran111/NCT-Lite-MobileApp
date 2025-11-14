package com.example.nct_lite.data.remote.api

import com.example.nct_lite.data.remote.model.response.AlbumListResponse
import com.example.nct_lite.data.remote.model.response.AlbumResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumApi {

    @GET("album")
    suspend fun getAlbums(): Response<AlbumListResponse>

    @GET("album/{id}")
    suspend fun getAlbumById(
        @Path("id") id: String
    ): Response<AlbumResponse>
}
