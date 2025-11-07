package com.example.nct_lite.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Xử lý khi chọn các mục trong thanh điều hướng
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Ở lại trang Home
                    true
                }
                R.id.navigation_search -> {
                    // TODO: mở Activity hoặc Fragment Search
                    // startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                R.id.navigation_library -> {
                    // TODO: mở Activity hoặc Fragment Library
                    // startActivity(Intent(this, LibraryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}