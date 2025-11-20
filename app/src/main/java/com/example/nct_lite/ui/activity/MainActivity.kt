package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

open class MainActivity : AppCompatActivity() {

    private lateinit var ivCover: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var btnPause: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMiniPlayer()
        setupBottomNav()
    }

    private fun setupMiniPlayer() {
        ivCover = findViewById(R.id.ivCover)
        tvTitle = findViewById(R.id.tvTitle)
        tvArtist = findViewById(R.id.tvArtist)
        btnPause = findViewById(R.id.btnPause)

        // Demo dữ liệu
        updateMiniPlayerUI(
            title = "Easy",
            artist = "Troye Sivan",
            coverUrl = "https://i.scdn.co/image/ab67616d0000b273581cb621f4ea544fb7d5bb30"
        )

        btnPause.setOnClickListener {
            // TODO: pause/play
        }

        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.musicBar)
            .setOnClickListener {
                startActivity(Intent(this, SongViewActivity::class.java))
            }
    }

    private fun updateMiniPlayerUI(title: String, artist: String, coverUrl: String?) {
        tvTitle.text = title
        tvArtist.text = artist

        if (!coverUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(coverUrl)
                .placeholder(R.drawable.ic_avatar_foreground)
                .into(ivCover)
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)

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
                    startActivity(Intent(this, LibraryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
