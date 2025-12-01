package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.nct_lite.data.SessionManager
import com.example.nct_lite.data.album.request.AlbumCreateRequest
import com.example.nct_lite.data.album.response.AlbumResponse
import com.example.nct_lite.databinding.BottomSheetNewPlaylistBinding
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NewPlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetNewPlaylistBinding? = null
    private val binding get() = _binding!!

    // Sử dụng activityViewModels để chia sẻ ViewModel với Fragment/Activity gọi nó
    private val albumViewModel: AlbumViewModel by activityViewModels { AlbumViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnCreate.setOnClickListener {
            val title = binding.editPlaylistName.text.toString().trim()
            val description = binding.editDescription.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên playlist", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val token = SessionManager.getToken(requireContext())
            if (token.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Lỗi xác thực, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnCreate.isEnabled = false
            val albumCreateRequest = AlbumCreateRequest(
                title = title,
                description = if (description.isNotEmpty()) description else null,
            )

            albumViewModel.createAlbum(
                albumCreateRequest
            )
        }

        albumViewModel.createAlbumResult.observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe
            result.onSuccess { response ->
                Toast.makeText(requireContext(), "Tạo playlist '${response.status}' thành công!", Toast.LENGTH_SHORT).show()
                dismiss()
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Tạo playlist thất bại: ${error.message}", Toast.LENGTH_LONG).show()
            }
            // Kích hoạt lại nút dù thành công hay thất bại
            binding.btnCreate.isEnabled = true
        }
    }
    fun setupListeners(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        albumViewModel.createAlbumResult.postValue(null) // Dọn dẹp LiveData để tránh trigger lại
        _binding = null
    }
}
