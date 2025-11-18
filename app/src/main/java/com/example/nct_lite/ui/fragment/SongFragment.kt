package com.example.nct_lite.ui.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nct_lite.R
import com.example.nct_lite.databinding.FragmentSongBinding
import com.example.nct_lite.viewmodel.SongViewModel

class SongFragment : Fragment(R.layout.fragment_song) {

    private val viewModel: SongViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var btnPlay: ImageButton
    private var isPlaying = false
    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSongBinding.bind(view)
        btnPlay = binding.btnPlay
        btnPlay.isEnabled = false

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        observeViewModel()

        val songId = arguments?.getString(ARG_SONG_ID).takeUnless { it.isNullOrBlank() }
            ?: DEFAULT_SONG_ID
        if (viewModel.songUrl.value.isNullOrEmpty()) {
            viewModel.loadSong(songId)
        }

        btnPlay.setOnClickListener { togglePlayPause() }
    }

    private fun observeViewModel() {
        viewModel.song.observe(viewLifecycleOwner) { song ->
            binding.songTitle.text = song.title
            binding.artistName.text = song.artist
            binding.albumTitle.text = song.title
        }

        viewModel.songUrl.observe(viewLifecycleOwner) { url ->
            if (url.isNullOrBlank()) {
                btnPlay.isEnabled = false
                btnPlay.setImageResource(R.drawable.ic_play_around)
                return@observe
            }
            prepareMusic(url)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun prepareMusic(url: String) {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                start()
                this@SongFragment.isPlaying = true
                btnPlay.isEnabled = true
                btnPlay.setImageResource(R.drawable.ic_pause_around)
            }
            setOnErrorListener { _, _, _ ->
                Toast.makeText(requireContext(), R.string.error_playing_song, Toast.LENGTH_SHORT)
                    .show()
                btnPlay.isEnabled = false
                btnPlay.setImageResource(R.drawable.ic_play_around)
                true
            }
        }
    }

    private fun togglePlayPause() {
        val player = mediaPlayer ?: return

        if (player.isPlaying) {
            player.pause()
            btnPlay.setImageResource(R.drawable.ic_play_around)
            isPlaying = false
        } else {
            player.start()
            btnPlay.setImageResource(R.drawable.ic_pause_around)
            isPlaying = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
        _binding = null
    }

    companion object {
        private const val ARG_SONG_ID = "song_id"
        private const val DEFAULT_SONG_ID = "1"

        fun newInstance(songId: String) = SongFragment().apply {
            arguments = bundleOf(ARG_SONG_ID to songId)
        }
    }
}
