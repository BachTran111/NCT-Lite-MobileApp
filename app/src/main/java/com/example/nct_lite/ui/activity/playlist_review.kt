package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.squareup.picasso.Picasso

data class PlaylistSong(
    val title: String,
    val artist: String,
    val coverUrl: String
)

class PlaylistReviewActivity : AppCompatActivity() {

    private val songList = listOf(
        PlaylistSong("Love Me Do", "The Beatles", "https://example.com/cover1.jpg"),
        PlaylistSong("From Me To You", "The Beatles", "https://example.com/cover2.jpg"),
        PlaylistSong("She Loves You", "The Beatles", "https://example.com/cover3.jpg"),
        PlaylistSong("I Want To Hold Your Hand", "The Beatles", "https://example.com/cover4.jpg")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playlist_review)

        val playlistContainer = findViewById<LinearLayout>(R.id.playlistsContainer)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        songList.forEach { song ->
            val itemView = layoutInflater.inflate(R.layout.item_song, playlistContainer, false)

            val imgCover = itemView.findViewById<ImageView>(R.id.imgCover)
            val tvTitle = itemView.findViewById<TextView>(R.id.tvSongTitle)
            val tvArtist = itemView.findViewById<TextView>(R.id.tvArtist)

            Picasso.get()
                .load(song.coverUrl)
                .placeholder(R.drawable.ic_avatar_foreground)
                .into(imgCover)

            tvTitle.text = song.title
            tvArtist.text = song.artist

            itemView.setOnClickListener {
                val intent = Intent(this, SongViewActivity::class.java)
                startActivity(intent)
            }

            playlistContainer.addView(itemView)
        }

        btnBack.setOnClickListener { finish() }
    }
}
