package com.example.nct_lite.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
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
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val songViewModel: SongViewModel by activityViewModels {
        SongViewModelFactory()
    }

//    private val songViewModel by lazy { ViewModelProvider(this)[SongViewModel::class.java] }
    private val albumViewModel by lazy { ViewModelProvider(this)[AlbumViewModel::class.java] }
    private val historyViewModel by lazy { ViewModelProvider(this)[HistoryViewModel::class.java] }

    private var cachedSongs: List<SongMetadata> = emptyList()

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
        observeData()
        songViewModel.loadAllSongs()
        historyViewModel.getHistory()
        albumViewModel.getAllAlbums()
    }

    private fun observeData() {
        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                cachedSongs = response.metadata
                populateQuickPickSongs(response.metadata)
                populateArtists(response.metadata)
            }
            result.onFailure { e -> showError("Error loading songs: ${e.message}") }
        }

//        historyViewModel.history.observe(viewLifecycleOwner) { result ->
//            result.onSuccess { response -> populateFavoriteSongs(response) }
//            result.onFailure { e -> showError("Error loading history: ${e.message}") }
//        }

//        albumViewModel.albums.observe(viewLifecycleOwner) { result ->
//            result.onSuccess { response ->
//                populateBestAlbums(response.metadata)
//            }
//            result.onFailure { e -> showError("Error loading albums: ${e.message}") }
//        }
    }

    private fun populateArtists(songs: List<SongMetadata>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val container = binding.containerArtists
        container.removeAllViews()

        val uniqueArtists = songs.map { it.artist }.distinct()
        uniqueArtists.forEach { artist ->
            val view = inflater.inflate(R.layout.item_artist, container, false)
            val nameView = view.findViewById<TextView>(R.id.text_artist_name)
            val imageView = view.findViewById<ImageView>(R.id.image_artist)

            nameView.text = artist
            val cover = songs.firstOrNull { it.artist == artist }?.coverUrl
            if (!cover.isNullOrEmpty()) {
                Picasso.get()
                    .load(cover)
                    .placeholder(R.drawable.placeholder_artist)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.placeholder_artist)
            }

            view.setOnClickListener {
//                val artistSongs = cachedSongs.filter { it.artist == artist }
//                if (artistSongs.isNotEmpty()) {
//                    startActivity(SongViewActivity.createIntent(requireContext(), artistSongs.first()))
//                } else {
//                    showError("Không có bài cho nghệ sĩ này")
//                }
                (activity as? MainActivity)?.openArtistPlaylist(artist)
            }

            container.addView(view)
        }
    }

    private fun populateQuickPickSongs(songs: List<SongMetadata>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        val row1 = binding.containerQuickPickRow1
        val row2 = binding.containerQuickPickRow2
        val row3 = binding.containerQuickPickRow3

        listOf(row1, row2, row3).forEach { it.removeAllViews() }
        val limitedSongs = songs.take(9)

        limitedSongs.chunked(3).forEachIndexed { index, chunk ->
            val target = when (index) {
                0 -> row1
                1 -> row2
                2 -> row3
                else -> null
            }
            target?.let{layout ->
            chunk.forEach { song ->
                val view = inflater.inflate(R.layout.item_quick_pick, target, false)
                val titleView = view.findViewById<TextView>(R.id.text_quick_title)
                val artistView = view.findViewById<TextView>(R.id.text_quick_artist)
                val imageView = view.findViewById<ImageView>(R.id.image_quick_pick)

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

                target.addView(view)
            }
            }
        }
    }

//    private fun populateFavoriteSongs(response: PlayHistoryResponse) {
//        val context = requireContext()
//        val container = binding.containerFavSongs
//        container.removeAllViews()
//
//        response.metadata.items.forEach { item ->
//            val textView = TextView(context).apply {
//                text = item.song.title
//                textSize = 14f
//                setTextColor(ContextCompat.getColor(context, android.R.color.white))
//                setPadding(16, 8, 16, 8)
//                setOnClickListener {
//                    val metadata = historySongToMetadata(item.song)
//                    startActivity(SongViewActivity.createIntent(requireContext(), metadata))
//                }
//            }
//            container.addView(textView)
//        }
//    }

//    private fun populateBestAlbums(albums: List<AlbumMetadata>) {
//        val context = requireContext()
//        val inflater = LayoutInflater.from(context)
//        val container = binding.containerBestAlbums
//        container.removeAllViews()
//
//        albums.forEach { album ->
//            val view = inflater.inflate(R.layout.item_album, container, false)
//            val imageView = view.findViewById<ImageView>(R.id.image_album)
//            val titleView = view.findViewById<TextView>(R.id.text_album_title)
//            val artistView = view.findViewById<TextView>(R.id.text_album_artist)
//
//            titleView.text = album.title
//            artistView.text = album.artist ?: "Unknown Artist"
//            val validCover = if (album.coverUrl.isNullOrEmpty()) null else album.coverUrl
//
//            Picasso.get()
//                .load(validCover)
//                .placeholder(R.drawable.placeholder_album)
//                .error(R.drawable.placeholder_album)
//                .fit().centerCrop()
//                .into(imageView)
//            view.setOnClickListener {
//                (activity as? MainActivity)?.openAlbumDetail(album.id)
//            }
//            container.addView(view)
//        }
//    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
