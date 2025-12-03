package com.example.nct_lite.data.song

import androidx.lifecycle.MutableLiveData
import com.example.nct_lite.data.ApiClient
import com.example.nct_lite.data.song.response.SongListResponse
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.data.song.response.SongResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SongRepository(
    private val remote: SongRemoteDataSource = SongRemoteDataSource(ApiClient.songApi)
) {
    private var currentQueue: List<SongMetadata> = emptyList()
    private var currentIndex: Int = -1

    val currentSong = MutableLiveData<SongMetadata?>()
    fun setPlaylist(songs: List<SongMetadata>, initialSongId: String) {
        currentQueue = songs
        currentIndex = songs.indexOfFirst { it._id == initialSongId }
        if (currentIndex != -1) {
            currentSong.postValue(currentQueue[currentIndex])
        }
    }
    fun getNextSong(): SongMetadata? {
        if (currentQueue.isEmpty()) return null

        currentIndex = (currentIndex + 1) % currentQueue.size

        val next = currentQueue[currentIndex]
        currentSong.postValue(next)
        return next
    }
    fun getPreviousSong(): SongMetadata? {
        if (currentQueue.isEmpty()) return null

        currentIndex = if (currentIndex - 1 < 0) currentQueue.size - 1 else currentIndex - 1

        val prev = currentQueue[currentIndex]
        currentSong.postValue(prev)
        return prev
    }
    fun getQueue(): List<SongMetadata> = currentQueue
    fun getCurrentSong(): SongMetadata? {
        if (currentIndex != -1 && currentQueue.isNotEmpty()) {
            return currentQueue[currentIndex]
        }
        return null
    }
    suspend fun getAllSongs(): Result<SongListResponse> {
        return try {
            val res = remote.getAllSongs()

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load songs"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSongById(id: String): Result<SongResponse> {
        return try {
            val res = remote.getSongById(id)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to load song $id"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchSongs(keyword: String): Result<SongListResponse> {
        return try {
            val res = remote.searchSongs(keyword)

            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception("Failed to search songs"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadSong(
        title: RequestBody,
        artist: RequestBody,
        genreIds: RequestBody,
        song: MultipartBody.Part,
        cover: MultipartBody.Part
    ): Result<SongResponse> {
        return try {
            val res = remote.uploadSong(title, artist, genreIds, song, cover)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception(res.errorBody()?.string() ?: "Upload failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPendingSongs(): Result<SongListResponse> {
        return try {
            val res = remote.getPendingSongs()
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception(res.errorBody()?.string() ?: "Failed to load pending songs"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun approveSong(id: String): Result<SongResponse> {
        return try {
            val res = remote.approveSong(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception(res.errorBody()?.string() ?: "Approve failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun rejectSong(id: String): Result<SongResponse> {
        return try {
            val res = remote.rejectSong(id)
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception(res.errorBody()?.string() ?: "Reject failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun likeSong(id: String): Result<Unit> {
        return try {
            val res = remote.likeSong(id)
            if (res.isSuccessful)
                Result.success(Unit)
            else
                Result.failure(Exception(res.errorBody()?.string() ?: "Like failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getMySongs(): Result<SongListResponse> {
        return try {
            val res = remote.getMySongs()
            if (res.isSuccessful && res.body() != null)
                Result.success(res.body()!!)
            else
                Result.failure(Exception(res.errorBody()?.string() ?: "Failed to load my songs"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
//    companion object {
//        private var instance: SongRepository? = null
//        fun getInstance(): SongRepository {
//            if (instance == null) {
//                instance = SongRepository()
//            }
//            return instance!!
//        }
//    }
    companion object {
        @Volatile
        private var instance: SongRepository? = null

        fun getInstance(): SongRepository {
            return instance ?: synchronized(this) {
                instance ?: SongRepository().also { instance = it }
            }
        }
    }
}
