package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val btnUpload = findViewById<Button>(R.id.button38)
        val btnBack = findViewById<Button>(R.id.buttonback)

        btnUpload.setOnClickListener {
            val intent = Intent(this, UserUploadMusicActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }

    }
}
