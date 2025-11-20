package com.example.spotify

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.example.nct_lite.R

class SongViewActivity : AppCompatActivity() {

    private var isPlaying = false

    // Views
    private lateinit var albumArt: ImageView
    private lateinit var albumTitle: TextView
    private lateinit var songTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var progressBar: SeekBar

    private lateinit var btnShuffle: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnPlay: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var btnRepeat: ImageButton
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.song_view)

        // Bind views
        albumArt = findViewById(R.id.album_art)
        albumTitle = findViewById(R.id.album_title)
        songTitle = findViewById(R.id.song_title)
        artistName = findViewById(R.id.artist_name)
        progressBar = findViewById(R.id.progress_bar)

        btnShuffle = findViewById(R.id.btn_shuffle)
        btnPrev = findViewById(R.id.btn_prev)
        btnPlay = findViewById(R.id.btn_play)
        btnNext = findViewById(R.id.btn_next)
        btnRepeat = findViewById(R.id.btn_repeat)
        btnBack = findViewById(R.id.btnBack)

        // Example: Load album image from URL
        val albumImageUrl = "https://example.com/album.jpg" // Thay bằng URL từ DB
        Picasso.get().load(albumImageUrl)
            .placeholder(R.drawable.ic_avatar_background)
            .into(albumArt)

        // Toggle play/pause
        btnPlay.setOnClickListener {
            isPlaying = !isPlaying
            btnPlay.setImageResource(if (isPlaying) R.drawable.ic_play_around else R.drawable.ic_pause_around)
        }

        // Back button
        btnBack.setOnClickListener { finish() }

        // Example Toast for other buttons
        btnNext.setOnClickListener { Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show() }
        btnPrev.setOnClickListener { Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show() }
        btnShuffle.setOnClickListener { Toast.makeText(this, "Shuffle", Toast.LENGTH_SHORT).show() }
        btnRepeat.setOnClickListener { Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show() }

        // ProgressBar listener
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
