package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R

class ChooseArtistsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_artists) // Đảm bảo tên layout đúng với file XML

        // Kiểm tra ID của các thành phần trong XML phải khớp với R.id.*
        val btnBack = findViewById<ImageButton>(R.id.btn_back) // Đảm bảo XML có ImageButton với ID btn_back
        val btnNext = findViewById<Button>(R.id.btn_next)      // Đảm bảo XML có Button với ID btn_next

        // Quay lại Signup hoặc Login
        btnBack.setOnClickListener {
            finish()
        }

        // Chuyển sang màn hình chính (MainActivity)
        btnNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}