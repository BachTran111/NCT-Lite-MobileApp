package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R

class UserUploadMusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.useruploadmusic)

        val btnback = findViewById<ImageButton>(R.id.btnBack)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val songname = findViewById<EditText>(R.id.edtSongName)
        val author = findViewById<EditText>(R.id.edtAuthor)
        val type = findViewById<EditText>(R.id.edtCategory)
        val mp4 = findViewById<EditText>(R.id.edtMp4)

        btnback.setOnClickListener {
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }

        btnClear.setOnClickListener {
            songname.text.clear()
            author.text.clear()
            type.text.clear()
            mp4.text.clear()
        }
    }
}