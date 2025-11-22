package com.example.nct_lite.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nct_lite.databinding.AdmineditsongBinding

class AdminEditSongFragment : Fragment() {

    private var _binding: AdmineditsongBinding? = null
    private val binding get() = _binding!!

    private val songId by lazy { arguments?.getString(ARG_ID).orEmpty() }
    private val songTitle by lazy { arguments?.getString(ARG_TITLE).orEmpty() }
    private val songArtist by lazy { arguments?.getString(ARG_ARTIST).orEmpty() }
    private val songGenres by lazy { arguments?.getString(ARG_GENRES).orEmpty() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdmineditsongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.edtSongName.setText(songTitle)
        binding.edtAuthor.setText(songArtist)
        binding.edtCategory.setText(songGenres)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ID = "arg_id"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_ARTIST = "arg_artist"
        private const val ARG_GENRES = "arg_genres"

        fun newInstance(
            id: String,
            title: String,
            artist: String,
            genres: String
        ): AdminEditSongFragment {
            return AdminEditSongFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id)
                    putString(ARG_TITLE, title)
                    putString(ARG_ARTIST, artist)
                    putString(ARG_GENRES, genres)
                }
            }
        }
    }
}
