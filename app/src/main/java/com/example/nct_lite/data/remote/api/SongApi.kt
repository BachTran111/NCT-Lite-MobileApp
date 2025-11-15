package com.example.nct_lite.data.remote.api

import com.example.nct_lite.data.remote.model.Song
import com.example.nct_lite.data.remote.model.response.SongListResponse
import com.example.nct_lite.data.remote.model.response.SongResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SongApi {

    @GET("songs")
    suspend fun getAllSongs(): Response<SongListResponse>

    @GET("songs/{id}")
    suspend fun getSongById(
        @Path("id") id: String
    ): Response<SongResponse>

    @GET("songs/search")
    suspend fun searchSongs(
        @Query("q") keyword: String
    ): Response<SongListResponse>
}
