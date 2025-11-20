package com.example.spotify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageButton
import android.content.Intent
import android.widget.EditText
import android.widget.TextView
import com.example.nct_lite.R

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val btnUpload = findViewById<Button>(R.id.button38)
        val btnBack = findViewById<Button>(R.id.buttonback)
        val btnArrow = findViewById<ImageButton>(R.id.arrowButton)

        btnUpload.setOnClickListener {
            val intent = Intent(this, useruploadmusic::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, user_library::class.java)
            startActivity(intent)
        }

    }
}
