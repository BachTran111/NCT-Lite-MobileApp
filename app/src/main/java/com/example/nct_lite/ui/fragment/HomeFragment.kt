package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nct_lite.databinding.FragmentHomeBinding
import com.example.nct_lite.viewmodel.AlbumViewModel
import com.example.nct_lite.viewmodel.GenreViewModel
import com.example.nct_lite.viewmodel.HistoryViewModel
import com.example.nct_lite.viewmodel.SongViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val songViewModel: SongViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by viewModels()
    private val genreViewModel: GenreViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Quan sát dữ liệu từ các ViewModel
        observeData()

        // Gọi API để load dữ liệu
        loadData()

        return root
    }

    private fun loadData() {
        // Load dữ liệu từ các API
        songViewModel.loadAllSongs() // Quick Pick Songs
        genreViewModel.getGenres() // Artists You Like
        historyViewModel.getHistory() // Favorite Songs
        albumViewModel.getAllAlbums() // Best Albums
    }

    private fun observeData() {
        // 1. Artists You Like (Genres)
        genreViewModel.genres.observe(viewLifecycleOwner) { result ->
            result.onFailure { e ->
                showError("Không thể tải danh sách nghệ sĩ: ${e.message}")
            }
            // TODO: Cập nhật UI khi có dữ liệu
        }

        // 2. Quick Pick Songs
        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onFailure { e ->
                showError("Không thể tải danh sách bài hát: ${e.message}")
            }
            // TODO: Cập nhật UI khi có dữ liệu
        }

        // 3. Favorite Songs (History)
        historyViewModel.history.observe(viewLifecycleOwner) { result ->
            result.onFailure { e ->
                showError("Không thể tải bài hát yêu thích: ${e.message}")
            }
            // TODO: Cập nhật UI khi có dữ liệu
        }

        // 4. Best Albums
        albumViewModel.albums.observe(viewLifecycleOwner) { result ->
            result.onFailure { e ->
                showError("Không thể tải danh sách album: ${e.message}")
            }
            // TODO: Cập nhật UI khi có dữ liệu
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}