package com.example.nct_lite.data.album

import com.example.nct_lite.data.album.request.AlbumCreateRequest
import com.example.nct_lite.data.album.response.AlbumListResponse
import com.example.nct_lite.data.album.response.AlbumResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AlbumApi {
    //    {{BASE_URL}}/api/albums/
    @GET("albums")
    suspend fun getAlbums(): Response<AlbumListResponse>

    @POST("albums")
    suspend fun createAlbum(
        @Body albumCreateRequest: AlbumCreateRequest
    ): Response<AlbumResponse>
    @GET("albums/featured")
    suspend fun getMyOwnALbum(): Response<AlbumListResponse>


    @GET("albums/{id}/songs")
    suspend fun getAlbumById(
        @Path("id") id: String
    ): Response<AlbumResponse>

    @PUT("albums/{id}/add-song")
    suspend fun addSongToAlbum(
        @Path("id") albumId: String,
        @retrofit2.http.Body songId: String
    ): Response<Unit>

    @DELETE("albums/{id}/remove-song/{songId}")
    suspend fun removeSongFromAlbum(
        @Path("id") albumId: String,
        @Path("songId") songId: String
    ): Response<Unit>

    @POST("albums/{id}/save")
    suspend fun saveAlbum(
        @Path("id") albumId: String
    ): Response<Unit>

    @DELETE("albums/{id}/unsave")
    suspend fun unsaveAlbum(
        @Path("id") albumId: String
    ): Response<Unit>

    @GET("albums/me/saved")
    suspend fun getSavedAlbums(): Response<AlbumListResponse>
}