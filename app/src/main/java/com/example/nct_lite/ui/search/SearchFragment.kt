package com.example.nct_lite.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.R
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.databinding.FragmentSearchBinding
import com.example.nct_lite.ui.activity.SongViewActivity
import com.example.nct_lite.viewmodel.song.SongViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val songViewModel by lazy { ViewModelProvider(this)[SongViewModel::class.java] }

    private var cachedSongs: List<SongMetadata> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSongs()
        songViewModel.loadAllSongs()

        binding.editSearch.doAfterTextChanged { text ->
            val keyword = text?.toString().orEmpty().trim()
            if (keyword.length < 2) {
                //                binding.textSearchResults.visibility = View.GONE
//                binding.containerSearchResults.removeAllViews()
                binding.textSearchResults.visibility = View.GONE

                // ⬅️ Lúc này mới render suggestion
                renderSongList(binding.containerSearchSuggestions, cachedSongs.take(6))

                binding.containerSearchResults.removeAllViews()
            } else {
                songViewModel.search(keyword)

            }
        }
    }

    private fun observeSongs() {
        songViewModel.songs.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                cachedSongs = response.metadata
//                renderSongList(binding.containerSearchSuggestions, cachedSongs.take(6))
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Không tải được danh sách bài hát", Toast.LENGTH_SHORT).show()
            }
        }

        songViewModel.searchResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                binding.textSearchResults.visibility = View.VISIBLE
                renderSongList(binding.containerSearchResults, response.metadata)
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Không tìm thấy bài hát phù hợp", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderSongList(container: LinearLayout, songs: List<SongMetadata>) {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        container.removeAllViews()

        songs.forEach { song ->
            val view = inflater.inflate(R.layout.item_playlist_song, container, false)
            val titleView = view.findViewById<TextView>(R.id.tvSongTitle)
            val artistView = view.findViewById<TextView>(R.id.tvArtist)
            val imageView = view.findViewById<ImageView>(R.id.imgCover)
            val moreBtn = view.findViewById<ImageView>(R.id.btnMore)

            titleView.text = song.title
            artistView.text = song.artist
            if (!song.coverUrl.isNullOrEmpty()) {
                com.squareup.picasso.Picasso.get()
                    .load(song.coverUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.ic_avatar_background)
            }

            view.setOnClickListener {
                startActivity(SongViewActivity.createIntent(requireContext(), song))
            }

            moreBtn.setOnClickListener {
                com.example.nct_lite.ui.fragment.BottomSheetSelectedFragment().show(parentFragmentManager, "BottomSheetSelected")
            }

            container.addView(view)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
