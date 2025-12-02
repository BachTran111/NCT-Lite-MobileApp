package com.example.nct_lite.data.song

import okhttp3.MultipartBody
import okhttp3.RequestBody

class SongRemoteDataSource(private val api: SongApi) {

    suspend fun getAllSongs() = api.getAllSongs()

    suspend fun getSongById(id: String) = api.getSongById(id)

    suspend fun searchSongs(keyword: String) = api.searchSongs(keyword, keyword, keyword)

    suspend fun uploadSong(
        title: RequestBody,
        artist: RequestBody,
        genreIds: RequestBody,
        song: MultipartBody.Part,
        cover: MultipartBody.Part
    ) = api.uploadSong(title, artist, genreIds, song, cover)

    suspend fun getPendingSongs() = api.getPendingSongs()

    suspend fun approveSong(id: String) = api.approveSong(id)

    suspend fun rejectSong(id: String) = api.rejectSong(id)

    suspend fun likeSong(id: String) = api.likeSong(id)

    suspend fun getMySongs() = api.getMySongs()
}
