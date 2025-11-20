package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.example.nct_lite.databinding.ActivityLibraryBinding
import com.example.nct_lite.viewmodel.PlaylistViewModel
import com.example.nct_lite.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private val contentBinding get() = binding.libraryContent

    private val userViewModel: UserViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        setupLibraryActions()
        observeUserData()
        observePlaylists()
    }

    private fun setupBottomNavigation() {
        binding.navView.selectedItemId = R.id.navigation_library
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    launchDestination(MainActivity::class.java)
                    true
                }
                R.id.navigation_search -> {
                    launchDestination(SearchActivity::class.java)
                    true
                }
                R.id.navigation_library -> true
                else -> false
            }
        }
    }

    private fun setupLibraryActions() {
        contentBinding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        contentBinding.btnMore.setOnClickListener {
            Toast.makeText(this, R.string.feature_updating, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchDestination(destination: Class<out AppCompatActivity>) {
        startActivity(Intent(this, destination).apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        })
        overridePendingTransition(0, 0)
    }

    private fun observeUserData() {
        userViewModel.user.observe(this) { user ->
            user?.let {
                Picasso.get()
                    .load(it.avatarUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(contentBinding.imgAvatar)

                contentBinding.tvPlaylistCount.text = it.playlistsCount.toString()
                contentBinding.tvFollowersCount.text = it.followersCount.toString()
                contentBinding.tvFollowingCount.text = it.followingCount.toString()
            }
        }

        userViewModel.error.observe(this) { message ->
            Toast.makeText(this, "Error loading user: $message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observePlaylists() {
        playlistViewModel.playlists.observe(this) { playlists ->
            val container = contentBinding.playlistsContainer
            container.removeAllViews()

            playlists.forEach { playlist ->
                val itemView = layoutInflater.inflate(R.layout.item_playlist, container, false)
                val img = itemView.findViewById<ImageView>(R.id.playlist_image)
                val name = itemView.findViewById<TextView>(R.id.playlist_name)

                Picasso.get()
                    .load(playlist.coverUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(img)

                name.text = playlist.name

                container.addView(itemView)
            }
        }

        playlistViewModel.error.observe(this) { message ->
            Toast.makeText(this, "Error loading playlists: $message", Toast.LENGTH_SHORT).show()
        }
    }
}

