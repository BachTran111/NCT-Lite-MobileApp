package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.example.nct_lite.ui.activity.PlaylistReviewActivity
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.viewmodel.genre.GenreViewModel
import com.example.nct_lite.viewmodel.genre.GenreViewModelFactory
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class LibraryActivity : AppCompatActivity() {

    private val albumViewModel: AlbumViewModel by viewModels { AlbumViewModelFactory() }
    private val songViewModel: SongViewModel by viewModels { SongViewModelFactory() }
    private val genreViewModel: GenreViewModel by viewModels { GenreViewModelFactory() }

    private lateinit var playlistsContainer: LinearLayout
    private lateinit var avatarView: ImageView
    private lateinit var playlistsCountView: TextView
    private lateinit var followersCountView: TextView
    private lateinit var followingCountView: TextView

    private var playlistsCount = 0
    private var followersCount = 0
    private var followingCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_library)

        playlistsContainer = findViewById(R.id.playlistsContainer)
        avatarView = findViewById(R.id.imgAvatar)
        playlistsCountView = findViewById(R.id.tvPlaylistsCount)
        followersCountView = findViewById(R.id.tvFollowersCount)
        followingCountView = findViewById(R.id.tvFollowingCount)

        val btnMore = findViewById<ImageButton>(R.id.btnMore)
        val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)

        btnMore.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        btnEditProfile.setOnClickListener {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show()
        }

        observeData()
        albumViewModel.getAllAlbums()
        songViewModel.loadAllSongs()
        genreViewModel.getGenres()
    }

    private fun observeData() {
        albumViewModel.albums.observe(this) { result ->
            result.onSuccess { response ->
                playlistsCount = response.metadata.size
                updateStats()
                updateAvatar(response.metadata)
                populatePlaylists(response.metadata)
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được danh sách playlist", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        songViewModel.songs.observe(this) { result ->
            result.onSuccess { response ->
                followersCount = response.metadata.size
                updateStats()
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được số lượng bài hát", Toast.LENGTH_SHORT).show()
            }
        }

        genreViewModel.genres.observe(this) { result ->
            result.onSuccess { response ->
                followingCount = response.metadata.size
                updateStats()
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được thể loại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStats() {
        playlistsCountView.text = playlistsCount.toString()
        followersCountView.text = followersCount.toString()
        followingCountView.text = followingCount.toString()
    }

    private fun updateAvatar(albums: List<AlbumMetadata>) {
        val coverUrl = albums.firstOrNull()?.coverUrl
        if (!coverUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(avatarView)
        } else {
            avatarView.setImageResource(R.drawable.ic_avatar_background)
        }
    }

    private fun populatePlaylists(albums: List<AlbumMetadata>) {
        playlistsContainer.removeAllViews()

        albums.forEach { album ->
            val itemView = layoutInflater.inflate(R.layout.item_playlist, playlistsContainer, false)
            val cover = itemView.findViewById<ImageView>(R.id.playlist_image)
            val title = itemView.findViewById<TextView>(R.id.playlist_name)

            title.text = album.title

            Picasso.get()
                .load(album.coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(cover)

            itemView.setOnClickListener {
                startActivity(Intent(this, PlaylistReviewActivity::class.java))
            }

            playlistsContainer.addView(itemView)
        }
    }
}
