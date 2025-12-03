package com.example.nct_lite.ui.queue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.databinding.BottomSheetQueueBinding // Đảm bảo layout là bottom_sheet_queue.xml
import com.example.nct_lite.ui.playlist.ItemPlaylistSongAdapter
import com.example.nct_lite.viewmodel.player.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QueueBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetQueueBinding? = null
    private val binding get() = _binding!!

    private val repo = SongRepository.getInstance()
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetQueueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentQueue = repo.getQueue()

        // --- THÊM LOG KIỂM TRA ---
        android.util.Log.d("QueueCheck", "Số bài hát trong hàng chờ: ${currentQueue.size}")

        if (currentQueue.isEmpty()) {
            binding.tvEmptyQueue.visibility = View.VISIBLE // Hiện text "Trống"
            binding.rvQueue.visibility = View.GONE
        } else {
            setupUI()
            setupRecyclerView()
            // ... setup adapter ...
        }
    }

    private fun setupUI() {
        binding.tvQueueTitle.text = "Danh sách phát"

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        val currentQueue = repo.getQueue()

        val currentSong = repo.getCurrentSong()

        if (currentQueue.isEmpty()) {
            binding.tvEmptyQueue.visibility = View.VISIBLE
            binding.rvQueue.visibility = View.GONE
            return
        } else {
            binding.tvEmptyQueue.visibility = View.GONE
            binding.rvQueue.visibility = View.VISIBLE
        }

        val adapter = ItemPlaylistSongAdapter(
            songs = currentQueue,
            onClick = { song ->
                playerViewModel.playSong(song)

                repo.setPlaylist(currentQueue, song._id)

                //  dismiss()
            },
            onMoreClick = { song, view ->
                //delete from queue
            }
        )

        binding.rvQueue.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            setHasFixedSize(true)
        }

        val index = currentQueue.indexOfFirst { it._id == currentSong?._id }
        if (index != -1) {
            binding.rvQueue.scrollToPosition(index)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}