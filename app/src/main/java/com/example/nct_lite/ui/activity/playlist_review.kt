package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        val btnPlay = findViewById<ImageButton>(R.id.btnPlay)

        btnPlay.setOnClickListener {
            val intent = Intent(this, SongViewActivity::class.java)
            startActivity(intent)
        }


        btnBack.setOnClickListener { finish() }
    }

    // Data class chứa thông tin bài hát, nhớ check r chỉnh sửa cho đúng theo DB nha, chỗ này chỉ lấy title với artist với Url th
    data class SongData(
        val title: String,
        val artist: String,
        val coverUrl: String
    )

    fun updateSongList(songList: List<SongData>) {
        val playlistContainer = findViewById<ScrollView>(R.id.playlistsContainer)

        songList.forEach { song ->
            // Inflate item_song.xml
            val itemView = layoutInflater.inflate(R.layout.item_playlist_song, playlistContainer, false)

            // Lấy các view con
            val imgCover = itemView.findViewById<ImageView>(R.id.imgCover)
            val tvTitle = itemView.findViewById<TextView>(R.id.tvSongTitle)
            val tvArtist = itemView.findViewById<TextView>(R.id.tvArtist)

            // Set dữ liệu
            tvTitle.text = song.title
            tvArtist.text = song.artist

            // Load hình ảnh bằng Picasso
            Picasso.get()
                .load(song.coverUrl)
                .placeholder(R.drawable.ic_avatar_foreground)
                .into(imgCover)

            // Xử lý click item (nếu muốn mở activity chi tiết)
            itemView.setOnClickListener {
                val intent = Intent(this, SongViewActivity::class.java)
                // Có thể truyền dữ liệu bài hát qua intent nếu cần
                intent.putExtra("songTitle", song.title)
                intent.putExtra("songArtist", song.artist)
                intent.putExtra("songCover", song.coverUrl)
                startActivity(intent)
            }

            // Thêm item vào container
            playlistContainer.addView(itemView)
        }
    }



}
