package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.ui.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetSelectedFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val song = arguments?.getSerializable(ARG_SONG) as? SongMetadata

        // Hiển thị thông tin bài hát
//        val tvSongTitle = view.findViewById<TextView>(R.id.tvSongTitle)
//        val tvArtistName = view.findViewById<TextView>(R.id.tvArtistName)
//        tvSongTitle.text = song?.title ?: "Không rõ bài hát"
//        tvArtistName.text = song?.artist ?: "Không rõ nghệ sĩ"

        view.findViewById<View>(R.id.action_add_to_playlist)?.setOnClickListener {
            // Thay vì đi qua một BottomSheet trung gian, gọi thẳng chức năng tạo playlist mới
            if (activity is MainActivity) {
                (activity as MainActivity).showNewPlaylistSheet()
            }
            if (activity is MainActivity) {
                (activity as MainActivity).showAddToPlaylist()
            }
            dismiss()
        }
        view.findViewById<View>(R.id.action_share)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.action_add_to_liked)?.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.action_download)?.setOnClickListener { dismiss() }
    }
    companion object {
        private const val ARG_SONG = "song_metadata"

        fun newInstance(song: SongMetadata): BottomSheetSelectedFragment {
            val fragment = BottomSheetSelectedFragment()
            val args = Bundle().apply {
                putSerializable(ARG_SONG, song)
            }
            fragment.arguments = args
            return fragment
        }
    }
}

