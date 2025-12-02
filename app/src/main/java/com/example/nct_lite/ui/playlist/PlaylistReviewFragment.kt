package com.example.nct_lite.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nct_lite.R
import com.example.nct_lite.data.album.response.AlbumMetadata
import com.example.nct_lite.data.song.response.SongMetadata
import com.example.nct_lite.databinding.PlaylistReviewBinding
import com.example.nct_lite.ui.activity.SongViewActivity
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.viewmodel.song.SongViewModel
import com.example.nct_lite.viewmodel.song.SongViewModelFactory
import com.squareup.picasso.Picasso

class PlaylistReviewFragment : Fragment() {

    private var _binding: PlaylistReviewBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by activityViewModels { AlbumViewModelFactory() }
    private val songViewModel: SongViewModel by activityViewModels { SongViewModelFactory() }

    private var albumId: String? = null
    private var currentAlbum: AlbumMetadata? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumId = arguments?.getString(ARG_ALBUM_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActions()
        observeData()

        albumId?.let { id ->
            albumViewModel.getAlbumById(id)
        }
    }

    private fun setupActions() {
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.btnPlay.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        albumViewModel.albumDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val album = response.metadata.album
                currentAlbum = album
                renderAlbumInfo(album)
                val songs = response.metadata.songs
                renderSongsList(songs)

            }.onFailure {
                Toast.makeText(context, "Failed to load data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        albumViewModel.updateAlbumResult.observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe

            binding.progressBar.visibility = View.GONE

            result.onSuccess {
                Toast.makeText(context, "Success to delete album", Toast.LENGTH_SHORT).show()
                albumViewModel.resetStatus()

                requireActivity().onBackPressedDispatcher.onBackPressed()

            }.onFailure {
                Toast.makeText(context, "Error to delete album: ${it.message}", Toast.LENGTH_SHORT).show()
                albumViewModel.resetStatus()
            }
        }
        albumViewModel.updateAlbumResult.observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe
            binding.progressBar.visibility = View.GONE
            result.onSuccess {
                Toast.makeText(context, "Success to update album!", Toast.LENGTH_SHORT).show()
                albumViewModel.resetStatus()
                albumId?.let { id ->
                    albumViewModel.getAlbumById(id)
                }
            }.onFailure {
                Toast.makeText(context, "Failed to update album: ${it.message}", Toast.LENGTH_SHORT).show()
                albumViewModel.resetStatus()
            }
        }
    }

    private fun renderAlbumInfo(album: AlbumMetadata) {
        binding.albumTitle.text = album.title
        binding.artistName.text = album.artist ?: "Unknown Artist"
        val year = album.releaseDate?.take(4) ?: ""
        binding.albumInfo.text = if (year.isNotEmpty()) "Album • $year" else "Album"

        val validCover = if (album.coverUrl.isNullOrEmpty()) null else album.coverUrl
        Picasso.get().load(validCover).placeholder(R.drawable.ic_avatar_foreground).into(binding.albumArt)
        val ivMore = binding.ivMore
        ivMore.setOnClickListener {
            showAlbumOptions(it, album)
        }
    }
    private fun renderSongsList(songs: List<SongMetadata>) {
        val adapter = ItemPlaylistSongAdapter(
            songs = songs,
            onClick = { song ->
                context?.let { ctx ->
                    startActivity(SongViewActivity.createIntent(ctx, song))
                }
            },
            onMoreClick = { song, view ->
//                com.example.nct_lite.ui.fragment.BottomSheetSelectedFragment.newInstance(song)
//                    .show(parentFragmentManager, "BottomSheetSelected")
                showSongOptions(song, view)
            }
        )
        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showSongOptions(song: SongMetadata, anchorView: View) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), anchorView)
        popup.menu.add(0, 1, 0, "Delete from Playlist")
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> {
                    confirmRemoveSong(song)
                    true
                }
                else -> false
            }
        }

        popup.show()
    }

    private fun confirmRemoveSong(song: SongMetadata) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete song from playlist")
            .setMessage("Do you want to remove '${song.title}' from this playlist?")
            .setPositiveButton("Delete") { _, _ ->
                // Gọi ViewModel để xóa
                val currentAlbumId = albumId
                if (currentAlbumId != null) {
                    // Hiện loading nếu cần
                    // binding.progressBar.visibility = View.VISIBLE
                    albumViewModel.moveSongFromAlbum(currentAlbumId, song._id)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showAlbumOptions(anchor: View, album: AlbumMetadata) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), anchor)
        popup.menu.add(0, 1, 0, "Edit information")
        popup.menu.add(0, 2, 1, "Delete Album")

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> { // Sửa
                    showEditAlbumDialog(album)
                    true
                }
                2 -> { // Xóa
                    showConfirmDeleteDialog(album)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    // 2. Hiển thị Dialog Xóa
    private fun showConfirmDeleteDialog(album: AlbumMetadata) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Delete Album")
            .setMessage("Do you want to delete album '${album.title}' No? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                albumViewModel.deleteAlbum(album.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showEditAlbumDialog(album: AlbumMetadata) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_album, null)
        val etTitle = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etAlbumTitle)
        val etDesc = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etAlbumDesc)
        etTitle.setText(album.title)
        etDesc.setText(album.description ?: "")
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Edit information")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = etTitle.text.toString().trim()
                val newDesc = etDesc.text.toString().trim()
                if (newTitle.isNotEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    albumViewModel.updateAlbum(
                        albumId = album.id,
                        title = newTitle,
                        artist = album.artist ?: "",
                        description = newDesc,
                        coverUrl = album.coverUrl ?: "",
                        isPublic = album.isPublic
                    )
                } else {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    companion object {
        private const val ARG_ALBUM_ID = "album_id"
        fun newInstance(albumId: String) = PlaylistReviewFragment().apply {
            arguments = Bundle().apply { putString(ARG_ALBUM_ID, albumId) }
        }
    }
}