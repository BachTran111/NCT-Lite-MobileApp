package com.example.nct_lite.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.adapters.QuickPickAdapter
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.databinding.FragmentArtistPlaylistBinding
import com.example.nct_lite.ui.activity.MainActivity
import com.example.nct_lite.ui.activity.SongViewActivity
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory

class ArtistPlaylistFragment : Fragment() {

    private var _binding: FragmentArtistPlaylistBinding? = null
    private val binding get() = _binding!!

    private val songViewModel: SongViewModel by activityViewModels {
        SongViewModelFactory()
    }

    private lateinit var songAdapter: QuickPickAdapter
    private var artistName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            artistName = it.getString(ARG_ARTIST_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        observeViewModel()

        artistName?.let {
            binding.progressBar.isVisible = true
            songViewModel.search(it)
        }
    }

    private fun setupUI() {
        binding.tvPlaylistTitle.text = artistName ?: "Playlist"
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        songAdapter = QuickPickAdapter(
            songs = emptyList(), // Tham số 1: List bài hát

            // Tham số 2: Xử lý click vào bài hát
            onSongClicked = { song ->
                val intent = SongViewActivity.createIntent(requireContext(), song)
                startActivity(intent)
            },

            // Tham số 3: Xử lý click nút 3 chấm (More)
            onMoreClicked = { song ->
                // Hiện BottomSheet tùy chọn
                (activity as? MainActivity)?.showSongOptions(song)
            }
        )

        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = songAdapter
        }
    }

    private fun observeViewModel() {
        songViewModel.searchResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.isVisible = false
            result?.onSuccess { response ->
                songAdapter.updateSongs(response.metadata)
            }?.onFailure {
                Toast.makeText(requireContext(), "Không thể tải danh sách bài hát", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        songViewModel.clearSearchResult() // Xóa kết quả tìm kiếm cũ
    }

    companion object {
        private const val ARG_ARTIST_NAME = "artist_name"
        fun newInstance(artistName: String) = ArtistPlaylistFragment().apply {
            arguments = Bundle().apply { putString(ARG_ARTIST_NAME, artistName) }
        }
    }
}