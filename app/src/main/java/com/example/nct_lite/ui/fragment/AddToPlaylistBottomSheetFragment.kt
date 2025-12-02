package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.R
import com.example.nct_lite.databinding.BottomSheetAddToPlaylistBinding
import com.example.nct_lite.ui.fragment.adapter.SimplePlaylistAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory

class AddToPlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddToPlaylistBinding? = null
    private val binding get() = _binding!!
    private val albumViewModel: AlbumViewModel by activityViewModels { AlbumViewModelFactory() }

    private var targetSongId: String? = null
    private lateinit var adapter: SimplePlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetSongId = arguments?.getString(ARG_SONG_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddToPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (targetSongId == null) {
            dismiss()
            return
        }

        setupRecyclerView()
        setupListeners()
        observeData()

        binding.progressBar.visibility = View.VISIBLE
        albumViewModel.getMyOwnAlbum()
    }

    private fun setupRecyclerView() {
        adapter = SimplePlaylistAdapter { selectedAlbum ->
            binding.progressBar.visibility = View.VISIBLE
            albumViewModel.addSongToAlbum(selectedAlbum.id, targetSongId!!)
        }

        binding.rvMyPlaylists.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AddToPlaylistBottomSheetFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.btnNewPlaylist.setOnClickListener {
            NewPlaylistBottomSheetFragment().show(parentFragmentManager, "NewPlaylistBottomSheet")
        }
    }

    private fun observeData() {
        albumViewModel.myAlbums.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            result.onSuccess { response ->
                adapter.submitList(response.metadata)
            }.onFailure {
                Toast.makeText(context, "Lỗi tải playlist", Toast.LENGTH_SHORT).show()
            }
        }
        albumViewModel.addSongResult.observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe

            binding.progressBar.visibility = View.GONE

            result.onSuccess {
                Toast.makeText(context, "Đã thêm vào playlist thành công!", Toast.LENGTH_SHORT).show()
                albumViewModel.resetAddSongResult // Reset để lần sau dùng tiếp
                dismiss() // Đóng BottomSheet này lại
            }.onFailure {
                Toast.makeText(context, "Thất bại: ${it.message}", Toast.LENGTH_SHORT).show()
                albumViewModel.resetAddSongResult
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddToPlaylistBottomSheetFragment"
        private const val ARG_SONG_ID = "song_id"

        fun newInstance(songId: String) = AddToPlaylistBottomSheetFragment().apply {
            arguments = Bundle().apply { putString(ARG_SONG_ID, songId) }
        }
    }
}
