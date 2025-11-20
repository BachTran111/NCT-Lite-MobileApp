package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nct_lite.R
import com.example.nct_lite.databinding.FragmentLibraryBinding
import com.example.nct_lite.viewmodel.PlaylistViewModel
import com.example.nct_lite.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUserData()
        observePlaylists()

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeUserData() {
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                Picasso.get()
                    .load(it.avatarUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(binding.imgAvatar)

                binding.tvPlaylistCount.text = it.playlistsCount.toString()
                binding.tvFollowersCount.text = it.followersCount.toString()
                binding.tvFollowingCount.text = it.followingCount.toString()
            }
        }

        userViewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), "Error loading user: $message", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observePlaylists() {
        playlistViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            val container = binding.playlistsContainer
            container.removeAllViews()

            playlists.forEach { playlist ->
                val itemView = layoutInflater.inflate(R.layout.item_playlist, container, false)
                val img = itemView.findViewById<ImageView>(R.id.playlist_image)
                val name = itemView.findViewById<TextView>(R.id.playlist_name)

                Picasso.get()
                    .load(playlist.coverUrl)
                    .placeholder(R.drawable.ic_avatar_background)
                    .into(img)

                name.text = playlist.name

                container.addView(itemView)
            }
        }

        playlistViewModel.error.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), "Error loading playlists: $message", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
