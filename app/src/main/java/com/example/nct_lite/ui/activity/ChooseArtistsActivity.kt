package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.nct_lite.R
import com.example.nct_lite.data.genre.model.Genre
import com.example.nct_lite.viewmodel.genre.GenreViewModel
import com.example.nct_lite.viewmodel.genre.GenreViewModelFactory

class ChooseArtistsActivity : AppCompatActivity() {

    private val genreViewModel: GenreViewModel by viewModels { GenreViewModelFactory() }

    private lateinit var artistGrid: GridLayout
    private lateinit var searchInput: EditText

    private var allArtists: List<Genre> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_artists)

        artistGrid = findViewById(R.id.grid_artists)
        searchInput = findViewById(R.id.et_search)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnNext = findViewById<Button>(R.id.btn_next)

        observeArtists()
        genreViewModel.getGenres()

        searchInput.doAfterTextChanged { text ->
            val keyword = text?.toString().orEmpty()
            filterArtists(keyword)
        }

        btnBack.setOnClickListener { finish() }

        btnNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    private fun observeArtists() {
        genreViewModel.genres.observe(this) { result ->
            result.onSuccess { response ->
                allArtists = response.metadata
                renderArtists(allArtists)
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được danh sách nghệ sĩ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun filterArtists(keyword: String) {
        if (keyword.isBlank()) {
            renderArtists(allArtists)
            return
        }

        val normalized = keyword.lowercase()
        val filtered = allArtists.filter { it.name.lowercase().contains(normalized) }
        renderArtists(filtered)
    }

    private fun renderArtists(artists: List<Genre>) {
        artistGrid.removeAllViews()

//        val inflater = layoutInflater
//        artists.forEach { artist ->
//            val view = inflater.inflate(R.layout.item_artist_choice, artistGrid, false)
//            val nameView = view.findViewById<TextView>(R.id.tvArtistName)
//            val avatar = view.findViewById<ImageView>(R.id.imgArtist)
//
//            nameView.text = artist.name
//            avatar.setImageResource(R.drawable.sample_artist)
//
//            artistGrid.addView(view)
//        }
    }
}
