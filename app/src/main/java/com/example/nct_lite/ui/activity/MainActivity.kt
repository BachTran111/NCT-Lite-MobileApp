package com.example.nct_lite.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.ui.admin.AdminEditSongFragment
import com.example.nct_lite.ui.artist.ArtistPlaylistFragment
import com.example.nct_lite.ui.admin.AdminManagingFragment
import com.example.nct_lite.ui.admin.AdminPreviewFragment
import com.example.nct_lite.ui.home.HomeFragment
import com.example.nct_lite.ui.library.LibraryFragment
import com.example.nct_lite.ui.fragment.AddToPlaylistBottomSheetFragment
import com.example.nct_lite.ui.fragment.NewPlaylistBottomSheet
//import com.example.nct_lite.ui.playlist.SongOptionsBottomSheet
import com.example.nct_lite.ui.fragment.BottomSheetSelectedFragment
import com.example.nct_lite.ui.playlist.PlaylistReviewFragment
import com.example.nct_lite.ui.search.SearchFragment
import com.example.nct_lite.ui.settings.SettingsFragment
import com.example.nct_lite.viewmodel.player.PlayerViewModelFactory
import com.example.nct_lite.viewmodel.player.PlayerViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import com.example.nct_lite.data.SessionManager


open class MainActivity : AppCompatActivity() {

    private val playerVM: PlayerViewModel by viewModels { PlayerViewModelFactory(application) }
    private var userRole: String? = null

    private lateinit var ivCover: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var btnPause: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userRole = intent.getStringExtra("USER_ROLE")
            ?: SessionManager.getRole(this)
        setupMiniPlayer()
        setupBottomNav()
        observeMiniPlayer()
        if (savedInstanceState == null) {
            showHomeFragment()
            findViewById<BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.navigation_home
        }
    }

    private fun setupMiniPlayer() {
//        val miniPlayer = findViewById<ConstraintLayout>(R.id.miniplayer)
    val musicBar = findViewById<ConstraintLayout>(R.id.musicBar)
//        val musicBar = miniPlayer.findViewById<ConstraintLayout>(R.id.musicBar)
        ivCover = musicBar.findViewById<ImageView>(R.id.ivCover)
        tvTitle = musicBar.findViewById<TextView>(R.id.tvTitle)
        btnPause = musicBar.findViewById<ImageButton>(R.id.btnPause)
        tvArtist = musicBar.findViewById(R.id.tvArtist)

        musicBar.setOnClickListener {
            val state = playerVM.playerState.value
            if (state.url.isNotEmpty()) {
                val fakeSong = SongMetadata(
                    _id = "",
                    title = state.title,
                    artist = state.artist,
                    genreIDs = emptyList(),
                    url = state.url,
                    coverUrl = state.coverUrl,
                    uploaderId = null,
                    createdAt = "",
                    updatedAt = ""
                )
                startActivity(SongViewActivity.createIntent(this, fakeSong))
            }
        }
        btnPause.setOnClickListener {
            playerVM.pauseOrResume()
        }
    }
    private fun observeMiniPlayer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerVM.playerState.collect { state ->
                    val bar = findViewById<ConstraintLayout>(R.id.musicBar)
                    if (state.title.isEmpty()) {
                        bar.visibility = View.GONE
                    } else {
                        bar.visibility = View.VISIBLE
                    }

                    tvTitle.text = state.title.ifEmpty { getString(R.string.app_name) }
                    tvArtist.text = state.artist
                    btnPause.setImageResource(
                        if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play_around
                    )

                    if (state.coverUrl.isNotEmpty()) {
                        Picasso.get()
                            .load(state.coverUrl)
                            .placeholder(R.drawable.ic_avatar_foreground)
                            .into(ivCover)
                    } else {
                        ivCover.setImageResource(R.drawable.ic_avatar_foreground)
                    }
                }
            }
        }
    }

    private fun showHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, HomeFragment(), "HomeFragment")
            .commit()
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    showHomeFragment()
                    true
                }
                R.id.navigation_search -> {
                    showSearchFragment()
                    true
                }
                R.id.navigation_library -> {
                    showLibraryFragment()
                    true
                }
                else -> false
            }
        }
    }

    fun openAlbumDetail(albumId: String) {
        val fragment = PlaylistReviewFragment.newInstance(albumId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, "PlaylistDetailFragment")
            .addToBackStack(null)
            .commit()
    }

    private fun showSearchFragment() {
        showFragment("SearchFragment") { SearchFragment() }
    }

    private fun showLibraryFragment() {
        showFragment("LibraryFragment") { LibraryFragment() }
    }

    fun openSettings() {
        showFragment("SettingsFragment") { SettingsFragment() }
    }

    fun isAdmin(): Boolean = userRole.equals("ADMIN", ignoreCase = true)

    fun openAdminPanel() {
        if (!isAdmin()) return
        showFragment("AdminManagingFragment") { AdminManagingFragment() }
    }

    fun openAdminPreview() {
        if (!isAdmin()) return
        showFragment("AdminPreviewFragment") { AdminPreviewFragment() }
    }

    fun openAdminEditSong(song: SongMetadata) {
        if (!isAdmin()) return
        val genres = song.genreIDs.joinToString { it.name }
        val fragment = AdminEditSongFragment.newInstance(song._id, song.title, song.artist, genres)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, "AdminEditSongFragment")
            .addToBackStack(null)
            .commit()
    }

    private fun showFragment(tag: String, factory: () -> Fragment) {
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: factory()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    fun playSong(song: SongMetadata) {
        playerVM.playSong(song)
    }

    fun openArtistPlaylist(artistName: String) {
        val fragment = ArtistPlaylistFragment.newInstance(artistName)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showSongOptions(song: SongMetadata) {
        val bottomSheet = BottomSheetSelectedFragment.newInstance(song)
        bottomSheet.show(supportFragmentManager, "SongOptionsSheet")
    }

    fun showAddToPlaylist(songId: String) {
        // Giả sử bạn có một BottomSheet để thêm vào playlist
        // val bottomSheet = AddToPlaylistBottomSheet.newInstance(songId)
        // bottomSheet.show(supportFragmentManager, "AddToPlaylistSheet")
        // Vì chưa có, ta sẽ mở thẳng NewPlaylist
        showNewPlaylistSheet()
    }

    fun showNewPlaylistSheet() {
        NewPlaylistBottomSheet().show(supportFragmentManager, "NewPlaylistSheet")
    }
}

