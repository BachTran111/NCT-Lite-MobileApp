package com.example.nct_lite.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class SearchActivity : AppCompatActivity() {

    private val songViewModel: SongViewModel by viewModels { SongViewModelFactory() }

    private lateinit var searchInput: EditText
    private lateinit var suggestionContainer: LinearLayout
    private lateinit var resultsContainer: LinearLayout
    private lateinit var resultsLabel: TextView

    private var cachedSongs: List<SongMetadata> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)

        searchInput = findViewById(R.id.edit_search)
        suggestionContainer = findViewById(R.id.container_search_suggestions)
        resultsContainer = findViewById(R.id.container_search_results)
        resultsLabel = findViewById(R.id.text_search_results)

        observeSongs()
        songViewModel.loadAllSongs()

        searchInput.doAfterTextChanged { text ->
            val keyword = text?.toString().orEmpty().trim()
            if (keyword.length >= 2) {
                songViewModel.search(keyword)
            } else {
                resultsLabel.visibility = View.GONE
                resultsContainer.removeAllViews()
            }
        }
    }

    private fun observeSongs() {
        songViewModel.songs.observe(this) { result ->
            result.onSuccess { response ->
                cachedSongs = response.metadata
                renderSongList(suggestionContainer, cachedSongs.take(6))
            }
            result.onFailure {
                Toast.makeText(this, "Không tải được danh sách bài hát", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        songViewModel.searchResult.observe(this) { result ->
            result.onSuccess { response ->
                resultsLabel.visibility = View.VISIBLE
                renderSongList(resultsContainer, response.metadata)
            }
            result.onFailure {
                Toast.makeText(this, "Không tìm thấy bài hát phù hợp", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun renderSongList(container: LinearLayout, songs: List<SongMetadata>) {
        container.removeAllViews()

        songs.forEach { song ->
            val itemView = layoutInflater.inflate(R.layout.item_playlist_song, container, false)
            val titleView = itemView.findViewById<TextView>(R.id.tvSongTitle)
            val artistView = itemView.findViewById<TextView>(R.id.tvArtist)
            val coverView = itemView.findViewById<ImageView>(R.id.imgCover)

            titleView.text = song.title
            artistView.text = song.artist

            if (!song.coverUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(song.coverUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(coverView)
            } else {
                coverView.setImageResource(R.drawable.ic_avatar_background)
            }

            itemView.setOnClickListener {
                startActivity(SongViewActivity.createIntent(this, song))
            }

            container.addView(itemView)
        }
    }
}
