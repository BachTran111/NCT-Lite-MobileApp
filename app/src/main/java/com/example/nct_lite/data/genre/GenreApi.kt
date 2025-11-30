package com.example.nct_lite.data.genre

import com.example.nct_lite.data.genre.response.GenreListResponse
//import com.example.nct_lite.data.genre.response.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface   GenreApi {

    @GET("genres")
    suspend fun getGenres(): Response<GenreListResponse>

    // @GET("genres/{id}")
    // suspend fun getGenreById(
    //     @Path("id") id: String
    // ): Response<GenreResponse>
}
