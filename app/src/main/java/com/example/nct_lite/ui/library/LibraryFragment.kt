package com.example.nct_lite.ui.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.R
import com.example.nct_lite.data.ApiClient.retrofit
import com.example.nct_lite.data.SessionManager
import com.example.nct_lite.data.album.AlbumApi
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.auth.AuthApi
import com.example.nct_lite.data.auth.AuthRemoteDataSource
import com.example.nct_lite.data.auth.AuthRepository
import com.example.nct_lite.databinding.FragmentLibraryBinding
import com.example.nct_lite.ui.activity.MainActivity
import com.example.nct_lite.ui.fragment.NewPlaylistBottomSheet
import com.example.nct_lite.ui.library.adapter.AlbumAdapter
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.viewmodel.auth.AuthViewModel
import com.example.nct_lite.viewmodel.auth.AuthViewModelFactory
import com.example.nct_lite.viewmodel.genre.GenreViewModel
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso


class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by lazy {
        val authApi = retrofit.create(AuthApi::class.java)
        val remote = AuthRemoteDataSource(authApi)
        val repository = AuthRepository(remote)
        val factory = AuthViewModelFactory(repository)
        ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }
    private val albumViewModel: AlbumViewModel by lazy {
        val albumApi = retrofit.create(AlbumApi::class.java)
        val repository = AlbumRepository(com.example.nct_lite.data.album.AlbumRemoteDataSource(albumApi))
        val factory = AlbumViewModelFactory(repository)
        ViewModelProvider(this, factory)[AlbumViewModel::class.java]
    }

    private val songViewModel: SongViewModel by activityViewModels { SongViewModelFactory(com.example.nct_lite.data.song.SongRepository()) }

    private val genreViewModel by lazy { ViewModelProvider(this)[GenreViewModel::class.java] }
    private lateinit var albumAdapter: AlbumAdapter
//    private val authViewModel by lazy { ViewModelProvider(this)[AuthViewModel::class.java] }

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
        setupRecyclerView()
        observeData()
        authViewModel.getInfor()
        val token = SessionManager.getToken(requireContext())
        if (!token.isNullOrEmpty()) {
            albumViewModel.getAllAlbums()
        } else {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập để xem playlist", Toast.LENGTH_SHORT).show()
        }
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
        binding.btnCreatePlaylist.setOnClickListener {
            NewPlaylistBottomSheet().show(parentFragmentManager, "NewPlaylistSheet")
        }
    }

    private fun setupRecyclerView() {
        albumAdapter = AlbumAdapter(mutableListOf()) { album: AlbumMetadata ->
            (activity as? MainActivity)?.openAlbumDetail(album._id)
        }
        binding.playlistsContainer.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeData() {
        albumViewModel.albums.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val albums = response.metadata
                Log.d("LibraryFragment", "Albums received: ${albums.size} items.")
                playlistsCount = albums.size
                updateStats()
                updateAvatar(albums)
                albumAdapter.updateList(albums)
                if (albums.isEmpty()) {
                    Log.d("LibraryFragment", "Album list is empty.")
                }
            }
            result.onFailure { error ->
                Log.e("LibraryFragment", "Failed to load albums", error)
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
        }
        authViewModel.username.observe(viewLifecycleOwner) {
            binding.tvUsername.text = it
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
