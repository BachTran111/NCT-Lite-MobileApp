package com.example.nct_lite.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.databinding.PlaylistReviewBinding
import com.example.nct_lite.ui.activity.SongViewActivity
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.squareup.picasso.Picasso

class PlaylistReviewFragment : Fragment() {

    private var _binding: PlaylistReviewBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel by lazy { ViewModelProvider(this)[AlbumViewModel::class.java] }
    private val songViewModel by lazy { ViewModelProvider(this)[SongViewModel::class.java] }

    private var albumId: String? = null
    private var albumMetadata: AlbumMetadata? = null
    private var allSongs: List<SongMetadata> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumId = arguments?.getString(ARG_ALBUM_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnPlay.setOnClickListener {
            // TODO: integrate player queue
        }

        observeData()
        albumId?.let { albumViewModel.getAlbumById(it) }
        songViewModel.loadAllSongs()
    }

    private fun observeData() {
        albumViewModel.albumDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                albumMetadata = response.metadata
                renderAlbum(response.metadata)
                renderSongs()
            }
        }
        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                allSongs = response.metadata
                renderSongs()
            }
        }
    }

    private fun renderAlbum(album: AlbumMetadata) {
        binding.albumTitle.text = album.title
        binding.artistName.text = album.artist
        binding.albumInfo.text = getString(
            R.string.album_year_template,
            album.releaseDate.orEmpty()
        )
        Picasso.get()
            .load(album.coverUrl)
            .placeholder(R.drawable.ic_avatar_background)
            .into(binding.albumArt)
    }

    private fun renderSongs() {
        val album = albumMetadata ?: return
        val container = binding.songsContainer
        container.removeAllViews()

        val inflater = LayoutInflater.from(requireContext())
        val albumSongs = allSongs.filter { album.songIDs.contains(it._id) }

        albumSongs.forEach { song ->
            val itemView = inflater.inflate(R.layout.item_playlist_song, container, false)
            val cover = itemView.findViewById<ImageView>(R.id.imgCover)
            val title = itemView.findViewById<TextView>(R.id.tvSongTitle)
            val artist = itemView.findViewById<TextView>(R.id.tvArtist)
            val moreBtn = itemView.findViewById<ImageView>(R.id.btnMore)

            title.text = song.title
            artist.text = song.artist
            Picasso.get()
                .load(song.coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(cover)

            itemView.setOnClickListener {
                startActivity(SongViewActivity.createIntent(requireContext(), song))
            }

            moreBtn.setOnClickListener {
                com.example.nct_lite.ui.fragment.BottomSheetSelectedFragment().show(parentFragmentManager, "BottomSheetSelected")
            }

            container.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ALBUM_ID = "album_id"

        fun newInstance(albumId: String): PlaylistReviewFragment {
            val fragment = PlaylistReviewFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_ALBUM_ID, albumId)
            }
            return fragment
        }
    }
}
