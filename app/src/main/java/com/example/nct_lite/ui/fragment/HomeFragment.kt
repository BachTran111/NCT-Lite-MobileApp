package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.genre.model.Genre
import com.example.nct_lite.data.history.response.PlayHistoryResponse
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.databinding.FragmentHomeBinding
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.viewmodel.genre.GenreViewModel
import com.example.nct_lite.viewmodel.genre.GenreViewModelFactory
import com.example.nct_lite.viewmodel.history.HistoryViewModel
import com.example.nct_lite.viewmodel.history.HistoryViewModelFactory
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val songViewModel: SongViewModel by viewModels { SongViewModelFactory() }
    private val albumViewModel: AlbumViewModel by viewModels { AlbumViewModelFactory() }
    private val genreViewModel: GenreViewModel by viewModels { GenreViewModelFactory() }
    private val historyViewModel: HistoryViewModel by viewModels { HistoryViewModelFactory() }

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
        genreViewModel.getGenres()  // Artists You Like
        historyViewModel.getHistory()  // Favorite Songs
        albumViewModel.getAllAlbums()  // Best Albums
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

    private fun populateQuickPickSongs(songs: List<SongMetadata>) {
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

    private fun populateBestAlbums(response: List<AlbumMetadata>) {
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
