package com.example.nct_lite.viewmodel.song

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.data.song.response.SongListResponse
import com.example.nct_lite.data.song.response.SongResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SongViewModel(
    // Nếu bạn đã làm Singleton cho Repo thì nên sửa thành: = SongRepository.getInstance()
    private val repo: SongRepository = SongRepository()
) : ViewModel() {

    // LiveData cho HomeFragment (tất cả bài hát)
    private val _songs = MutableLiveData<Result<SongListResponse>>()
    val songs: LiveData<Result<SongListResponse>> get() = _songs

    // LiveData cho chi tiết bài hát
    private val _songDetail = MutableLiveData<Result<SongResponse>>()
    val songDetail: LiveData<Result<SongResponse>> get() = _songDetail

    // LiveData cho kết quả tìm kiếm
    private val _searchResult = MutableLiveData<Result<SongListResponse>?>()
    val searchResult: LiveData<Result<SongListResponse>?> get() = _searchResult

    // LiveData cho Playlist (Load danh sách theo ID)
    private val _playlistSongs = MutableLiveData<Result<SongListResponse>>()
    val playlistSongs: LiveData<Result<SongListResponse>> = _playlistSongs

    fun loadAllSongs() {
        viewModelScope.launch {
            _songs.postValue(repo.getAllSongs())
        }
    }

    fun getSongById(id: String) {
        viewModelScope.launch {
            _songDetail.postValue(repo.getSongById(id))
        }
    }

    fun search(keyword: String) {
        viewModelScope.launch {
            _searchResult.postValue(repo.searchSongs(keyword))
        }
    }

    fun clearSearchResult() {
        _searchResult.value = null
    }

    fun loadSongsFromIdList(songIds: List<String>) {
        if (songIds.isEmpty()) {
            val emptyResponse = SongListResponse(
                status = "OK",
                message = "Empty list",
                metadata = emptyList()
            )
            _playlistSongs.postValue(Result.success(emptyResponse))
            return
        }

        viewModelScope.launch {
            try {
                val results = songIds.map { id ->
                    async {
                        repo.getSongById(id)
                    }
                }.awaitAll()

                val validSongs = results.mapNotNull { result ->
                    result.getOrNull()?.metadata
                }

                val finalResponse = SongListResponse(
                    status = "OK",
                    message = "Loaded from IDs",
                    metadata = validSongs
                )

                _playlistSongs.postValue(Result.success(finalResponse))

            } catch (e: Exception) {
                _playlistSongs.postValue(Result.failure(e))
            }
        }
    }
}