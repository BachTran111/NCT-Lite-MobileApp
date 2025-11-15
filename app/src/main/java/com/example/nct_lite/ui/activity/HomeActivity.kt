//package com.example.nct_lite.ui.activity
//
//import android.os.Bundle
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.isVisible
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.nct_lite.R
//import com.example.nct_lite.data.remote.model.Album
//import com.example.nct_lite.databinding.HomeBinding
//import com.example.nct_lite.viewmodel.AlbumViewModel
//import com.example.nct_lite.viewmodel.GenreViewModel
//import com.example.nct_lite.viewmodel.HistoryViewModel
//import com.example.nct_lite.viewmodel.SongViewModel
//import com.example.nct_lite.data.remote.model.Song
//import com.example.nct_lite.data.remote.model.response.AlbumResponse
//import com.example.nct_lite.data.remote.model.response.GenreListResponse
//import com.example.nct_lite.data.remote.model.response.PlayHistoryResponse
//
//class HomeActivity : AppCompatActivity() {
//
//    private lateinit var binding: HomeBinding
//    private val songViewModel: SongViewModel by viewModels()
//    private val albumViewModel: AlbumViewModel by viewModels()
//    private val genreViewModel: GenreViewModel by viewModels()
//    private val historyViewModel: HistoryViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = HomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Lấy dữ liệu từ các ViewModel
//        observeData()
//
//        // Gọi API khi tạo Activity
//        songViewModel.loadAllSongs() // Quick Pick Songs
//        genreViewModel.getGenres() // Artists You Like
//        historyViewModel.getHistory() // Favorite Songs
//        albumViewModel.getAllAlbums() // Best Albums
//    }
//
//    // Xử lý cập nhật UI khi các ViewModel trả về dữ liệu
//    private fun observeData() {
//        // 1. Artists You Like
//        genreViewModel.genres.observe(this) { result ->
//            result.onSuccess { response ->
//                populateArtists(response)
//            }
//            result.onFailure { e ->
//                Toast.makeText(this, "Error loading genres: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // 2. Quick Pick Songs
//        songViewModel.songs.observe(this) { result ->
//            result.onSuccess { response ->
//                populateQuickPickSongs(response.metadata)
//            }
//            result.onFailure { e ->
//                Toast.makeText(this, "Error loading songs: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // 3. Favorite Songs
//        historyViewModel.history.observe(this) { result ->
//            result.onSuccess { response ->
//                populateFavoriteSongs(response)
//            }
//            result.onFailure { e ->
//                Toast.makeText(this, "Error loading favorite songs: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        // 4. Best Albums
//        albumViewModel.albums.observe(this) { result ->
//            result.onSuccess { response ->
//                populateBestAlbums(response.metadata)
//            }
//            result.onFailure { e ->
//                Toast.makeText(this, "Error loading albums: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // Hiển thị danh sách Artists You Like
//    private fun populateArtists(response: GenreListResponse) {
//        val container = binding.containerArtists
//        container.removeAllViews()
//        response.metadata.forEach { genre ->
//            val textView = TextView(this).apply {
//                text = genre.name
//                textSize = 16f
//                setTextColor(resources.getColor(android.R.color.white))
//                setPadding(16, 8, 16, 8)
//            }
//            container.addView(textView)
//        }
//    }
//
//    // Hiển thị danh sách Quick Pick Songs
//    private fun populateQuickPickSongs(songs: List<Song>) {
//        val row1Container = binding.containerQuickPickRow1
//        val row2Container = binding.containerQuickPickRow2
//        val row3Container = binding.containerQuickPickRow3
//
//        row1Container.removeAllViews()
//        row2Container.removeAllViews()
//        row3Container.removeAllViews()
//
//        songs.chunked(3).forEachIndexed { index, chunk ->
//            val container = when (index) {
//                0 -> row1Container
//                1 -> row2Container
//                else -> row3Container
//            }
//
//            chunk.forEach { song ->
//                val songView = TextView(this).apply {
//                    text = song.title
//                    textSize = 14f
//                    setTextColor(resources.getColor(android.R.color.white))
//                    setPadding(16, 8, 16, 8)
//                }
//                container.addView(songView)
//            }
//        }
//    }
//
//    // Hiển thị danh sách Favorite Songs (từ PlayHistory)
//    private fun populateFavoriteSongs(response: PlayHistoryResponse) {
//        val container = binding.containerFavSongs
//        container.removeAllViews()
//
//        response.metadata.items.forEach { historyItem ->
//            val songView = TextView(this).apply {
//                text = historyItem.song.title
//                textSize = 14f
//                setTextColor(resources.getColor(android.R.color.white))
//                setPadding(16, 8, 16, 8)
//            }
//            container.addView(songView)
//        }
//    }
//
//    // Hiển thị danh sách Best Albums
//    private fun populateBestAlbums(response: List<Album>) {
//        val container = binding.containerBestAlbums
//        container.removeAllViews()
//
//        response.forEach { album ->
//            val albumView = TextView(this).apply {
//                text = album.title
//                textSize = 14f
//                setTextColor(resources.getColor(android.R.color.white))
//                setPadding(16, 8, 16, 8)
//            }
//            container.addView(albumView)
//        }
//    }
//
//}
