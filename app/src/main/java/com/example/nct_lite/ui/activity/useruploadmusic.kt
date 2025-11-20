package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        val btnUpload = findViewById<Button>(R.id.btnUpload)

        btnback.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        btnClear.setOnClickListener {
            songname.text.clear()
            author.text.clear()
            type.text.clear()
            mp4.text.clear()
        }

        btnUpload.setOnClickListener {
            val songData = getSongData()
            // Lưu trữ dữ liệu bài hát vào cơ sở dữ liệu
        }
    }

    data class SongData(
        val name: String,
        val author: String,
        val category: String,
        val mp4File: String
    )

    private fun getSongData(): SongData {
        val songName = findViewById<EditText>(R.id.edtSongName).text.toString().trim()
        val author = findViewById<EditText>(R.id.edtAuthor).text.toString().trim()
        val category = findViewById<EditText>(R.id.edtCategory).text.toString().trim()
        val mp4 = findViewById<EditText>(R.id.edtMp4).text.toString().trim()

        return SongData(
            name = songName,
            author = author,
            category = category,
            mp4File = mp4
        )
        // Phần này là dữ liệu nhập vào trả về dưới dạng SongData ở trên nè, khi connect nếu sai kiểu nhớ sửa lại nha
    }
}