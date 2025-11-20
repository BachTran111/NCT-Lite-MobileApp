package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.example.nct_lite.data.remote.model.Album
import com.example.nct_lite.data.remote.model.Genre
import com.example.nct_lite.data.remote.model.Song
import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse
import com.example.nct_lite.databinding.ActivityMainBinding
import com.example.nct_lite.viewmodel.AlbumViewModel
import com.example.nct_lite.viewmodel.GenreViewModel
import com.example.nct_lite.viewmodel.HistoryViewModel
import com.example.nct_lite.viewmodel.SongViewModel

/**
 * MainActivity hiện đóng vai trò màn hình Home chính.
 * Mỗi tab trong BottomNavigationView sẽ mở một Activity riêng
 * để thay thế hoàn toàn việc sử dụng Fragment.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeBinding get() = binding.homeContent

    private val songViewModel: SongViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by viewModels()
    private val genreViewModel: GenreViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.title_home)

        setupBottomNavigation()
        observeData()

        songViewModel.loadAllSongs()
        genreViewModel.getGenres()
        historyViewModel.getHistory()
        albumViewModel.getAllAlbums()
    }

    private fun setupBottomNavigation() {
        binding.navView.selectedItemId = R.id.navigation_home
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_search -> {
                    launchDestination(SearchActivity::class.java)
                    true
                }
                R.id.navigation_library -> {
                    launchDestination(LibraryActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }

    private fun launchDestination(destination: Class<out AppCompatActivity>) {
        startActivity(Intent(this, destination).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        })
        overridePendingTransition(0, 0)
    }

    private fun observeData() {
        genreViewModel.genres.observe(this) { result ->
            result.onSuccess { response -> populateArtists(response.metadata) }
            result.onFailure { e -> showError("Error loading genres: ${e.message}") }
        }

        songViewModel.songs.observe(this) { result ->
            result.onSuccess { response -> populateQuickPickSongs(response.metadata) }
            result.onFailure { e -> showError("Error loading songs: ${e.message}") }
        }

        historyViewModel.history.observe(this) { result ->
            result.onSuccess { response -> populateFavoriteSongs(response) }
            result.onFailure { e -> showError("Error loading favorite songs: ${e.message}") }
        }

        albumViewModel.albums.observe(this) { result ->
            result.onSuccess { response -> populateBestAlbums(response.metadata) }
            result.onFailure { e -> showError("Error loading albums: ${e.message}") }
        }
    }

    private fun populateArtists(response: List<Genre>) {
        val container = homeBinding.containerArtists
        container.removeAllViews()
        response.forEach { genre ->
            val genreView = TextView(this).apply {
                text = genre.name
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 8, 16, 8)
            }
            container.addView(genreView)
        }
    }

    private fun populateQuickPickSongs(songs: List<Song>) {
        val row1Container = homeBinding.containerQuickPickRow1
        val row2Container = homeBinding.containerQuickPickRow2
        val row3Container = homeBinding.containerQuickPickRow3

        row1Container.removeAllViews()
        row2Container.removeAllViews()
        row3Container.removeAllViews()

        songs.chunked(3).forEachIndexed { index, chunk ->
            val container = when (index) {
                0 -> row1Container
                1 -> row2Container
                else -> row3Container
            }

            chunk.forEach { song ->
                val songView = TextView(this).apply {
                    text = song.title
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.white))
                    setPadding(16, 8, 16, 8)
                }
                container.addView(songView)
            }
        }
    }

    private fun populateFavoriteSongs(response: PlayHistoryResponse) {
        val container = homeBinding.containerFavSongs
        container.removeAllViews()

        response.metadata.items.forEach { historyItem ->
            val songView = TextView(this).apply {
                text = historyItem.song.title
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 8, 16, 8)
            }
            container.addView(songView)
        }
    }

    private fun populateBestAlbums(response: List<Album>) {
        val container = homeBinding.containerBestAlbums
        container.removeAllViews()

        response.forEach { album ->
            val albumView = TextView(this).apply {
                text = album.title
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 8, 16, 8)
            }
            container.addView(albumView)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
