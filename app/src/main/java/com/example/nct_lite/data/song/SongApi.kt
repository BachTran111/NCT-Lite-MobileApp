package com.example.nct_lite.data.song

import com.example.nct_lite.data.song.response.SongListResponse
import com.example.nct_lite.data.song.response.SongResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
//        @Query("keyword") query: String
        @Query("artist") artist: String?,
        @Query("title") title: String?,
        @Query("genre") genre: String?    ): Response<SongListResponse>
//    Response<SongListResponse>
    @Multipart
    @POST("songs")
    suspend fun uploadSong(
        @Part("title") title: RequestBody,
        @Part("artist") artist: RequestBody,
        @Part("genreIDs") genreIds: RequestBody,
        @Part song: MultipartBody.Part,
        @Part cover: MultipartBody.Part
    ): Response<SongResponse>

    @GET("songs/me")
    suspend fun getMySongs(): Response<SongListResponse>

    @GET("songs/pending")
    suspend fun getPendingSongs(): Response<SongListResponse>

    @PUT("songs/{id}/approve")
    suspend fun approveSong(
        @Path("id") id: String
    ): Response<SongResponse>

    @DELETE("songs/{id}/reject")
    suspend fun rejectSong(
        @Path("id") id: String
    ): Response<SongResponse>

    @POST("songs/{id}/like")
    suspend fun likeSong(
        @Path("id") id: String
    ): Response<Unit>
}
