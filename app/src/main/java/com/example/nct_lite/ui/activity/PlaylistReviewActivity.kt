package com.example.nct_lite.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class PlaylistReviewActivity : AppCompatActivity() {

    private val songViewModel: SongViewModel by viewModels { SongViewModelFactory() }
    private val albumViewModel: AlbumViewModel by viewModels { AlbumViewModelFactory() }

    private lateinit var albumArt: ImageView
    private lateinit var albumTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var albumInfo: TextView
    private lateinit var songsContainer: LinearLayout
    private lateinit var btnBack: ImageButton
    private lateinit var btnPlay: ImageButton

    private var currentSongs: List<SongMetadata> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playlist_review)

        albumArt = findViewById(R.id.albumArt)
        albumTitle = findViewById(R.id.albumTitle)
        artistName = findViewById(R.id.artistName)
        albumInfo = findViewById(R.id.albumInfo)
        btnBack = findViewById(R.id.btnBack)
        btnPlay = findViewById(R.id.btnPlay)
        songsContainer = findViewById(R.id.songsContainer)

        observeData()
        albumViewModel.getAllAlbums()
        songViewModel.loadAllSongs()

        btnBack.setOnClickListener { finish() }
        btnPlay.setOnClickListener {
            currentSongs.firstOrNull()?.let { song ->
                startActivity(SongViewActivity.createIntent(this, song))
            } ?: Toast.makeText(this, "Chưa có bài hát để phát", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        albumViewModel.albums.observe(this) { result ->
            result.onSuccess { response ->
                response.metadata.firstOrNull()?.let { updateAlbumInfo(it) }
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được album", Toast.LENGTH_SHORT).show()
            }
        }

        songViewModel.songs.observe(this) { result ->
            result.onSuccess { response ->
                currentSongs = response.metadata
                populateSongs(response.metadata)
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được danh sách bài hát", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAlbumInfo(album: AlbumMetadata) {
        albumTitle.text = album.title
        artistName.text = album.artist
        albumInfo.text = "Album • ${album.releaseDate.takeIf { !it.isNullOrBlank() } ?: "NCT-Lite"}"

        Picasso.get()
            .load(album.coverUrl)
            .placeholder(R.drawable.ic_avatar_background)
            .into(albumArt)
    }

    private fun populateSongs(songs: List<SongMetadata>) {
        songsContainer.removeAllViews()

        songs.forEach { song ->
            val view = layoutInflater.inflate(R.layout.item_playlist_song, songsContainer, false)
            val title = view.findViewById<TextView>(R.id.tvSongTitle)
            val artist = view.findViewById<TextView>(R.id.tvArtist)
            val cover = view.findViewById<ImageView>(R.id.imgCover)

            title.text = song.title
            artist.text = song.artist

            if (!song.coverUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(song.coverUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(cover)
            } else {
                cover.setImageResource(R.drawable.ic_avatar_background)
            }

            view.setOnClickListener {
                startActivity(SongViewActivity.createIntent(this, song))
            }

            songsContainer.addView(view)
        }
    }
}
