package com.example.nct_lite.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.databinding.FragmentLibraryBinding
import com.example.nct_lite.ui.activity.MainActivity
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.genre.GenreViewModel
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class LibraryFragment : Fragment() {

//    private var _binding: UserLibraryBinding? = null
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
//    private val authViewModel: AuthViewModel by lazy {
//        val authApi = retrofit.create(AuthApi::class.java)
//        val remote = AuthRemoteDataSource(authApi)
//        val repository = AuthRepository(remote)
//        val factory = AuthViewModelFactory(repository)
//        ViewModelProvider(this, factory)[AuthViewModel::class.java]
//    }

    private val albumViewModel by lazy { ViewModelProvider(this)[AlbumViewModel::class.java] }
    private val songViewModel: SongViewModel by viewModels {
        SongViewModelFactory()
    }
    private val genreViewModel by lazy { ViewModelProvider(this)[GenreViewModel::class.java] }

    private var playlistsCount = 0
    private var followersCount = 0
    private var followingCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        observeData()
//        authViewModel.getInfor()
        albumViewModel.getAllAlbums()
        songViewModel.loadAllSongs()
        genreViewModel.getGenres()
    }

    private fun setupActions() {
        binding.btnMore.setOnClickListener {
            (activity as? MainActivity)?.openSettings()
        }
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        albumViewModel.albums.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                playlistsCount = response.metadata.size
                updateStats()
                updateAvatar(response.metadata)
                populatePlaylists(response.metadata)
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Không tải được danh sách playlist", Toast.LENGTH_SHORT).show()
            }
        }

        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                followersCount = response.metadata.size
                updateStats()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Không tải được số lượng bài hát", Toast.LENGTH_SHORT).show()
            }
        }

        genreViewModel.genres.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                followingCount = response.metadata.size
                updateStats()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Không tải được thể loại", Toast.LENGTH_SHORT).show()
            }
//            authViewModel.username.observe(viewLifecycleOwner) {
//                binding.tvUsername.text = it
//            }
        }
    }

    private fun updateStats() {
        binding.tvPlaylistCount.text = playlistsCount.toString()
        binding.tvFollowersCount.text = followersCount.toString()
        binding.tvFollowingCount.text = followingCount.toString()
    }

    private fun updateAvatar(albums: List<AlbumMetadata>) {
        val coverUrl = albums.firstOrNull()?.coverUrl
        if (!coverUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(binding.imgAvatar)
        } else {
            binding.imgAvatar.setImageResource(R.drawable.ic_avatar_background)
        }
    }

    private fun populatePlaylists(albums: List<AlbumMetadata>) {
        val container = binding.playlistsContainer
        container.removeAllViews()

        albums.forEach { album ->
            val itemView = layoutInflater.inflate(R.layout.item_playlist, container, false)
            val cover = itemView.findViewById<android.widget.ImageView>(R.id.playlist_image)
            val title = itemView.findViewById<android.widget.TextView>(R.id.playlist_name)

            title.text = album.title

            Picasso.get()
                .load(album.coverUrl)
                .placeholder(R.drawable.ic_avatar_background)
                .into(cover)

            itemView.setOnClickListener {
                (activity as? MainActivity)?.openAlbumDetail(album._id)
            }

            container.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
