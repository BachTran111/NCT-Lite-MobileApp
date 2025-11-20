package com.example.nct_lite.ui.library

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.example.nct_lite.ui.fragment.HomeFragment
import com.example.nct_lite.ui.search.SearchFragment
import com.example.nct_lite.viewmodel.PlaylistViewModel
import com.example.nct_lite.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import android.content.Intent


class LibraryActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_library)

        observeUserData()
        observePlaylists()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }

                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }

                R.id.navigation_library -> {
                    true
                }

                else -> false
            }
        }

    }

    /** Render user info */
    private fun observeUserData() {
        userViewModel.user.observe(this) { user ->

            val imgAvatar = findViewById<ImageView>(R.id.imgAvatar)
            Picasso.get()
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(imgAvatar)

            val statsLayout = findViewById<LinearLayout>(R.id.statsLayout)

            if (statsLayout.childCount >= 3) {
                val playlistsCountText =
                    (statsLayout.getChildAt(0) as LinearLayout).getChildAt(0) as TextView
                playlistsCountText.text = user.playlistsCount.toString()

                val followersCountText =
                    (statsLayout.getChildAt(1) as LinearLayout).getChildAt(0) as TextView
                followersCountText.text = user.followersCount.toString()

                val followingCountText =
                    (statsLayout.getChildAt(2) as LinearLayout).getChildAt(0) as TextView
                followingCountText.text = user.followingCount.toString()
            }
        }

        userViewModel.error.observe(this) { message ->
            Toast.makeText(this, "Error loading user: $message", Toast.LENGTH_SHORT).show()
        }
    }

    /** Render danh sách playlist */
    private fun observePlaylists() {
        val container = findViewById<LinearLayout>(R.id.playlistsContainer)

        playlistViewModel.playlists.observe(this) { playlists ->
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
