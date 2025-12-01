package com.example.nct_lite.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.history.response.PlayHistoryResponse
import com.example.nct_lite.data.song.model.Song
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.databinding.FragmentHomeBinding
import com.example.nct_lite.ui.activity.MainActivity
import com.example.nct_lite.ui.activity.SongViewActivity
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.history.HistoryViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.example.nct_lite.viewmodel.song.SongViewModel
import androidx.fragment.app.activityViewModels
import com.example.nct_lite.data.SessionManager
import com.squareup.picasso.Picasso
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val songViewModel: SongViewModel by activityViewModels { SongViewModelFactory(com.example.nct_lite.data.song.SongRepository()) }

    private val albumViewModel by lazy { ViewModelProvider(this)[AlbumViewModel::class.java] }
    private val historyViewModel by lazy { ViewModelProvider(this)[HistoryViewModel::class.java] }

    private lateinit var artistAdapter: ArtistAdapter
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeData()
        songViewModel.loadAllSongs()
        historyViewModel.getHistory()
        val token = SessionManager.getToken(requireContext())
        if (!token.isNullOrEmpty()) {
            albumViewModel.getAllAlbums()
        }
    }

    private fun setupRecyclerViews() {
        // Artist Adapter
        artistAdapter = ArtistAdapter { artistName ->
            (activity as? MainActivity)?.openArtistPlaylist(artistName)
        }
        (binding.containerArtists as? RecyclerView)?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = artistAdapter
        }

        // Album Adapter
        albumAdapter = AlbumAdapter { album ->
            // Handle album click if needed, e.g., open album details
        }
        (binding.containerBestAlbums as? RecyclerView)?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
    }

    private fun observeData() {
        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                // FIX 1: Giới hạn số lượng bài hát hiển thị Quick Pick (ví dụ: 12 bài)
                // Nếu không, nó sẽ inflate hàng trăm view và gây lag
                val limitedSongs = response.metadata.take(12)
                populateQuickPickSongs(limitedSongs)

                // Phần xử lý Artist này bạn làm RẤT TỐT (đã đẩy sang Default thread)
                lifecycleScope.launch {
                    val uniqueArtists = withContext(Dispatchers.Default) {
                        response.metadata
                            .filter { !it.artist.isNullOrBlank() } // Safety check
                            .distinctBy { it.artist }
                            .map { song ->
                                ArtistAdapter.ArtistData(
                                    name = song.artist,
                                    coverUrl = song.coverUrl
                                )
                            }
                    }
                    artistAdapter.submitList(uniqueArtists)
                }
            }
            result.onFailure { e -> showError("Error loading songs: ${e.message}") }
        }

        historyViewModel.history.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response -> populateFavoriteSongs(response) }
            result.onFailure { e -> showError("Error loading history: ${e.message}") }
        }

        albumViewModel.albums.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                albumAdapter.submitList(response.metadata)
            }
            result.onFailure { e -> showError("Error loading albums: ${e.message}") }
        }
    }

    private fun populateQuickPickSongs(songs: List<SongMetadata>) {
        val context = context ?: return // Safe check context
        val inflater = LayoutInflater.from(context)
        val row1 = binding.containerQuickPickRow1
        val row2 = binding.containerQuickPickRow2
        val row3 = binding.containerQuickPickRow3

        // Xóa view cũ để tránh bị duplicate khi data update
        listOf(row1, row2, row3).forEach { it?.removeAllViews() }

        // Logic chia hàng
        songs.chunked(3).forEachIndexed { index, chunk ->
            val target = when (index) {
                0 -> row1
                1 -> row2
                2 -> row3
                else -> null // FIX 2: Bỏ qua các hàng thừa, chỉ fill 3 hàng đầu
            }

            // Nếu target khác null thì mới add view
            target?.let { layout ->
                chunk.forEach { song ->
                    val view = inflater.inflate(R.layout.item_quick_pick, layout, false)
                    val titleView = view.findViewById<TextView>(R.id.tvSongTitle)
                    val artistView = view.findViewById<TextView>(R.id.tvArtist)
                    val imageView = view.findViewById<ImageView>(R.id.imgCover)

                    titleView.text = song.title
                    artistView.text = song.artist
                    if (!song.coverUrl.isNullOrEmpty()) {
                        Picasso.get()
                            .load(song.coverUrl)
                            .placeholder(R.drawable.placeholder_quick)
                            .into(imageView)
                    } else {
                        imageView.setImageResource(R.drawable.placeholder_quick)
                    }

                    view.setOnClickListener {
                        startActivity(SongViewActivity.createIntent(requireContext(), song))
                    }

                    layout.addView(view)
                }
            }
        }}

    private fun populateFavoriteSongs(response: PlayHistoryResponse) {
        val context = requireContext()
        val container = binding.containerFavSongs
        container.removeAllViews()

        response.metadata.items.forEach { item ->
            val textView = TextView(context).apply {
                text = item.song.title
                textSize = 14f
                setTextColor(ContextCompat.getColor(context, android.R.color.white))
                setPadding(16, 8, 16, 8)
                setOnClickListener {
                    val metadata = historySongToMetadata(item.song)
                    startActivity(SongViewActivity.createIntent(requireContext(), metadata))
                }
            }
            container.addView(textView)
        }
    }

    private fun historySongToMetadata(song: Song): SongMetadata {
        return SongMetadata(
            _id = song._id,
            title = song.title,
            artist = song.artist,
            genreIDs = emptyList(),
            url = song.url,
            coverUrl = song.coverUrl,
            uploaderId = null,
            createdAt = "",
            updatedAt = ""
        )
    }

    private fun showError(message: String) {
        if (isAdded && context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
