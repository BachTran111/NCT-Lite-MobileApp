package com.example.nct_lite.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.databinding.PlaylistReviewBinding
import com.example.nct_lite.ui.activity.SongViewActivity
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class PlaylistReviewFragment : Fragment() {

    private var _binding: PlaylistReviewBinding? = null
    private val binding get() = _binding!!

    // ViewModel setup tương tự LibraryFragment
//    private val albumViewModel by lazy { ViewModelProvider(this)[AlbumViewModel::class.java] }
//    private val songViewModel by lazy { ViewModelProvider(this)[SongViewModel::class.java] }
    private val albumViewModel: AlbumViewModel by activityViewModels { AlbumViewModelFactory() }
    private val songViewModel: SongViewModel by activityViewModels { SongViewModelFactory() }
    private var albumId: String? = null
    private var albumMetadata: AlbumMetadata? = null

    // Lưu danh sách tất cả bài hát để lọc sau
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
//        setupActions()
//        setupUI()
        // Gọi API
        observeData()
//        albumId?.let { albumViewModel.getAlbumById(it) }
//        songViewModel.loadAllSongs()
// 1. Setup Observe
        observeData()

        // 2. Kích hoạt lấy Album
        albumId?.let { id ->
            albumViewModel.getAlbumById(id)
        }
    }

    private fun setupActions() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnPlay.setOnClickListener {
            Toast.makeText(requireContext(), "Chức năng phát tất cả đang phát triển", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        // 1. Lấy thông tin chi tiết Album
        albumViewModel.albumDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val album = response.metadata
                renderAlbumInfo(album)
// --- BƯỚC 2: CHUYỀN BÓNG SANG SONG VIEW MODEL ---
                // Lấy danh sách ID từ album và yêu cầu SongViewModel đi tải
                val listIds = album.songIDs ?: emptyList()// Đây là List<String>

                // Hiện loading trong lúc chờ tải bài hát
//                binding.progressBar.visibility = View.VISIBLE

                // Gọi hàm chúng ta vừa viết ở Bước 1
                songViewModel.loadSongsFromIdList(listIds)
            }
            result.onFailure {
                // Xử lý lỗi tải album
            }
        }
//        albumViewModel.getAlbumById()

        // 2. Lấy danh sách tất cả bài hát (để lọc xem bài nào thuộc album này)
//        songViewModel.songs.observe(viewLifecycleOwner) { result ->
//            result.onSuccess { response ->
//                allSongs = response.metadata
//                renderSongsList(allSongs) // Render lại list khi tải xong bài hát
//            }
//        }
        // --- BƯỚC 3: HỨNG DANH SÁCH BÀI HÁT ĐÃ TẢI XONG ---
        songViewModel.playlistSongs.observe(viewLifecycleOwner) { result ->
//            binding.progressBar.visibility = View.GONE // Tắt loading

            result.onSuccess { response -> // Đây là List<SongMetadata> hoàn chỉnh
                // Render danh sách ra màn hình
                renderSongsList(response.metadata)
            }
            result.onFailure {
                Toast.makeText(context, "Lỗi tải bài hát", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderAlbumInfo(album: AlbumMetadata) {
        binding.albumTitle.text = album.title
        binding.artistName.text = album.artist ?: "Unknown Artist"
        binding.albumInfo.text = getString(
            R.string.album_year_template,
            album.releaseDate ?: "Unknown"
        )
        val validCover = if (album.coverUrl.isNullOrEmpty()) null else album.coverUrl
        Picasso.get()
            .load(validCover)
            .placeholder(R.drawable.ic_avatar_foreground)
            .error(R.drawable.ic_avatar_foreground)
            .into(binding.albumArt)
    }

    private fun renderSongsList(songs: List<SongMetadata>) {
        // Phải đảm bảo có cả Metadata Album và List bài hát thì mới render
//        val album = albumMetadata ?: return
//        if (allSongs.isEmpty()) return

        val container = binding.songsContainer
        container.removeAllViews() // Xóa view cũ để tránh trùng lặp

        val inflater = LayoutInflater.from(requireContext())

        // LỌC BÀI HÁT: Chỉ lấy những bài có ID nằm trong danh sách songIDs của Album
//        val songList = albumViewModel.getAlbumById(album._id)
//        val albumSongs = allSongs.filter { album.songIDs.contains(it._id) }

        // Nếu không có bài nào
        if (songs.isEmpty()) {
            // Có thể hiển thị text "Chưa có bài hát" nếu muốn
            return
        }

        songs.forEach { song ->
            // Inflate layout item (item_playlist_song)
            val itemView = inflater.inflate(R.layout.item_playlist_song, container, false)

            val cover = itemView.findViewById<ImageView>(R.id.imgCover)
            val title = itemView.findViewById<TextView>(R.id.tvSongTitle)
            val artist = itemView.findViewById<TextView>(R.id.tvArtist)
            val moreBtn = itemView.findViewById<ImageView>(R.id.btnMore)

            title.text = song.title
            artist.text = song.artist

            // Xử lý ảnh bài hát an toàn
            val validSongCover = if (song.coverUrl.isNullOrEmpty()) null else song.coverUrl
            Picasso.get()
                .load(validSongCover)
                .placeholder(R.drawable.ic_avatar_background)
                .error(R.drawable.ic_avatar_background)
                .into(cover)

            // Sự kiện Click vào bài hát
            itemView.setOnClickListener {
//                startActivity(SongViewActivity.createIntent(requireContext(), song))
                context?.let { ctx ->
                    startActivity(SongViewActivity.createIntent(ctx, song))
                }
            }

            // Sự kiện nút More (...)
            moreBtn.setOnClickListener {
                com.example.nct_lite.ui.fragment.BottomSheetSelectedFragment()
                    .show(parentFragmentManager, "BottomSheetSelected")
            }

            // Thêm view vào LinearLayout container
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