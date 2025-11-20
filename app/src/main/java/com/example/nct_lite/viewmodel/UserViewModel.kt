package com.example.nct_lite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nct_lite.data.remote.model.User

class UserViewModel : ViewModel() {

    // LiveData chính để fragment quan sát
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    // LiveData để báo lỗi (nếu cần)
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        // Khởi tạo dữ liệu demo (nếu chưa gọi API)
        loadUserData()
    }

    /** Hàm giả lập load dữ liệu user */
    private fun loadUserData() {
        try {
            // Thông tin user demo
            val demoUser = User(
                avatarUrl = "https://example.com/avatar.png",
                playlistsCount = 23,
                followersCount = 58,
                followingCount = 43,
                _id ="1",
                username ="thai",
                role ="admin"
            )
            _user.value = demoUser
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    /** Hàm cập nhật dữ liệu user từ API hoặc database */
    fun updateUserData(newUser: User) {
        _user.value = newUser
    }
}
