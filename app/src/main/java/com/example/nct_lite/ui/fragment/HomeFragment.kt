package com.example.nct_lite.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nct_lite.R
import com.example.nct_lite.data.remote.model.Album
import com.example.nct_lite.data.remote.model.Genre
import com.example.nct_lite.data.remote.model.Song
import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse
import com.example.nct_lite.databinding.FragmentHomeBinding
import com.example.nct_lite.ui.search.SearchFragment
import com.example.nct_lite.viewmodel.AlbumViewModel
import com.example.nct_lite.viewmodel.GenreViewModel
import com.example.nct_lite.viewmodel.HistoryViewModel
import com.example.nct_lite.viewmodel.SongViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val songViewModel: SongViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by viewModels()
    private val genreViewModel: GenreViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()

        // Gọi API khi Fragment được tạo
        songViewModel.loadAllSongs()  // Quick Pick Songs
        genreViewModel.getGenres()    // Artists You Like
        historyViewModel.getHistory() // Favorite Songs
        albumViewModel.getAllAlbums() // Best Albums

        val bottomNav = binding.bottomNavigation

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_search -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment())
                        .commit()
                    true
                }
                R.id.navigation_library -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LibraryFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }



    // Xử lý cập nhật UI khi các ViewModel trả về dữ liệu
    private fun observeData() {
        genreViewModel.genres.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response -> populateArtists(response.metadata) }
            result.onFailure { e -> showError("Error loading genres: ${e.message}") }
        }

        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response -> populateQuickPickSongs(response.metadata) }
            result.onFailure { e -> showError("Error loading songs: ${e.message}") }
        }

        historyViewModel.history.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response -> populateFavoriteSongs(response) }
            result.onFailure { e -> showError("Error loading favorite songs: ${e.message}") }
        }

        albumViewModel.albums.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response -> populateBestAlbums(response.metadata) }
            result.onFailure { e -> showError("Error loading albums: ${e.message}") }
        }
    }

    private fun populateArtists(response: List<Genre>) {
        val container = binding.containerArtists
        container.removeAllViews()
        response.forEach { genre ->
            val genreView = TextView(context).apply {
                text = genre.name
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 8, 16, 8)
            }
            container.addView(genreView)
        }
    }

    private fun populateQuickPickSongs(songs: List<Song>) {
        val row1Container = binding.containerQuickPickRow1
        val row2Container = binding.containerQuickPickRow2
        val row3Container = binding.containerQuickPickRow3

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
                val songView = TextView(context).apply {
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
        val container = binding.containerFavSongs
        container.removeAllViews()

        response.metadata.items.forEach { historyItem ->
            val songView = TextView(context).apply {
                text = historyItem.song.title
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 8, 16, 8)
            }
            container.addView(songView)
        }
    }

    private fun populateBestAlbums(response: List<Album>) {
        val container = binding.containerBestAlbums
        container.removeAllViews()

        response.forEach { album ->
            val albumView = TextView(context).apply {
                text = album.title
                textSize = 14f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(16, 8, 16, 8)
            }
            container.addView(albumView)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding when view is destroyed
    }



}
