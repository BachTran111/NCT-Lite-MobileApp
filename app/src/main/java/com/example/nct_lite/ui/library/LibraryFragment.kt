package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    // ViewModels
    private val albumViewModel by lazy { ViewModelProvider(this)[AlbumViewModel::class.java] }
    private val songViewModel: SongViewModel by viewModels { SongViewModelFactory() }
    private val genreViewModel by lazy { ViewModelProvider(this)[GenreViewModel::class.java] }

    // Adapter RecyclerView
    private lateinit var libraryAdapter: LibraryAdapter

    // Biến lưu dữ liệu tạm để gộp
    private var myAlbumsList: List<AlbumMetadata> = emptyList()
    private var savedAlbumsList: List<AlbumMetadata> = emptyList()

    // Biến thống kê
    private var playlistsCount = 0
    private var followersCount = 0
    private var followingCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView() // 1. Cài đặt RecyclerView
        setupActions()      // 2. Cài đặt nút bấm
        observeData()       // 3. Lắng nghe dữ liệu

        // 4. Gọi API
        albumViewModel.getMyOwnAlbum()
        albumViewModel.getSavedAlbum()

        // Gọi API phụ cho thống kê
        songViewModel.loadAllSongs()
        genreViewModel.getGenres()
    }

    private fun setupRecyclerView() {
        // Khởi tạo Adapter
        libraryAdapter = LibraryAdapter { album ->
            // Xử lý khi click vào 1 playlist
            // Chú ý: Dùng album.id hoặc album._id tùy vào DataClass của bạn
            (activity as? MainActivity)?.openAlbumDetail(album.id)
        }

        // Gán vào RecyclerView trong XML
        binding.rvPlaylists.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = libraryAdapter
            setHasFixedSize(true) // Tối ưu hiệu năng
        }
    }

    private fun setupActions() {
        binding.btnMore.setOnClickListener {
            (activity as? MainActivity)?.openSettings()
        }
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng đang phát triển", Toast.LENGTH_SHORT).show()
        }
        binding.btnCreatePlaylist.setOnClickListener {
            (activity as? MainActivity)?.showNewPlaylistSheet()
        }
    }

    private fun observeData() {
        // --- LUỒNG 1: Album của tôi ---
        albumViewModel.myAlbums.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                // Cập nhật thống kê & Avatar (Chỉ lấy từ album của tôi)
                playlistsCount = response.metadata.size
                updateStats()
                updateAvatar(response.metadata)

                // Cập nhật list và refresh adapter
                myAlbumsList = response.metadata
                updateLibraryAdapter()
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Lỗi tải playlist của bạn", Toast.LENGTH_SHORT).show()
            }
        }

        // --- LUỒNG 2: Album đã lưu ---
        albumViewModel.savedAlbums.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                // Cập nhật list và refresh adapter
                savedAlbumsList = response.metadata
                updateLibraryAdapter()
            }
        }

        // --- Các luồng phụ (Thống kê) ---
        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                followersCount = response.metadata.size
                updateStats()
            }
        }

        genreViewModel.genres.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                followingCount = response.metadata.size
                updateStats()
            }
        }
    }

    // Hàm cập nhật Adapter: Gộp 2 list lại rồi đưa vào Adapter
    private fun updateLibraryAdapter() {
        libraryAdapter.submitData(myAlbumsList, savedAlbumsList)
    }

    private fun updateStats() {
        binding.tvPlaylistCount.text = playlistsCount.toString()
        binding.tvFollowersCount.text = followersCount.toString()
        binding.tvFollowingCount.text = followingCount.toString()
    }

    private fun updateAvatar(albums: List<AlbumMetadata>) {
        val coverUrl = albums.firstOrNull()?.coverUrl
        val validCover = if (coverUrl.isNullOrEmpty()) null else coverUrl

        Picasso.get()
            .load(validCover)
            .placeholder(R.drawable.ic_avatar_background)
            .into(binding.imgAvatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}