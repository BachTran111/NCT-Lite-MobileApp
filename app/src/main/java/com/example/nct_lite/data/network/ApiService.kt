package com.example.nct_lite.data.network
import com.example.nct_lite.data.model.Song
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("songs")
    fun getAllSongs(): Call<List<Song>>

    @GET("songs/{id}")
    fun getSongDetail(@Path("id") id: Int): Call<Song>

    @GET("songs")
    fun searchSongs(@Query("keyword") keyword: String): Call<List<Song>>

    @GET("songs")
    fun getSongsByGenre(@Query("genre") genre: String): Call<List<Song>>

    @GET("songs/featured")
    fun getFeaturedSongs(): Call<List<Song>>

    @GET("songs/top-rated")
    fun getTopRatedSongs(): Call<List<Song>>
    @GET("song/{id}")
    suspend fun getSong(
        @Path("id") id: String
    ): Song

}


    