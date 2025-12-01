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

    // --- HÀM BẠN CẦN SỬA ---
    fun loadSongsFromIdList(songIds: List<String>) {
        // 1. Xử lý trường hợp danh sách ID rỗng
        if (songIds.isEmpty()) {
            // Phải tạo một SongListResponse rỗng giả lập
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
                // 2. Chạy song song để tải từng bài hát
                val results = songIds.map { id ->
                    async {
                        repo.getSongById(id) // Trả về Result<SongResponse>
                    }
                }.awaitAll()

                // 3. Trích xuất dữ liệu: "Gỡ" vỏ Result và vỏ SongResponse để lấy SongMetadata
                val validSongs = results.mapNotNull { result ->
                    // getOrNull(): Nếu lỗi thì bỏ qua (trả về null)
                    // .metadata: Lấy thông tin bài hát từ SongResponse
                    result.getOrNull()?.metadata
                }

                // 4. Đóng gói lại thành SongListResponse (Quan trọng!)
                // Vì LiveData mong đợi SongListResponse chứ không phải List<SongMetadata>
                val finalResponse = SongListResponse(
                    status = "OK",
                    message = "Loaded from IDs",
                    metadata = validSongs // Gán list bài hát vào đây
                )

                _playlistSongs.postValue(Result.success(finalResponse))

            } catch (e: Exception) {
                _playlistSongs.postValue(Result.failure(e))
            }
        }
    }
}