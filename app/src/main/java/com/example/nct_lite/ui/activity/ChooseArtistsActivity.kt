package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R

/**
 * ChooseArtistsActivity - Màn hình chọn nghệ sĩ yêu thích
 *
 * Flow:
 * 1. SignupActivity -> ChooseArtistsActivity (sau khi đăng ký thành công)
 * 2. ChooseArtistsActivity -> MainActivity (sau khi chọn artists và nhấn Next)
 *    MainActivity hiện là màn hình Home chính thay vì dùng Fragment.
 */
class ChooseArtistsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_artists)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnNext = findViewById<Button>(R.id.btn_next)

        // Quay lại màn hình trước (SignupActivity)
        btnBack.setOnClickListener {
            finish()
        }

        // Chuyển đến MainActivity - MainActivity đã hiển thị trực tiếp nội dung Home
        btnNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // Clear các activity phía trên trong stack để người dùng không thể quay lại
            // StartActivity và SignupActivity bằng nút back
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish() // Đóng ChooseArtistsActivity
        }
    }
}