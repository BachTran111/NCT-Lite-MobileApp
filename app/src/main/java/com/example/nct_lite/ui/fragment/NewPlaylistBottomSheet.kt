package com.example.nct_lite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nct_lite.data.SessionManager
import androidx.fragment.app.activityViewModels
import com.example.nct_lite.viewmodel.album.AlbumViewModelFactory
import com.example.nct_lite.databinding.BottomSheetNewPlaylistBinding
import com.example.nct_lite.viewmodel.album.AlbumViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class NewPlaylistBottomSheet : BottomSheetDialogFragment() {

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

            // Vô hiệu hóa nút để tránh click nhiều lần
            binding.btnCreate.isEnabled = false

            // Chuyển đổi String thành RequestBody
            val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            // Cung cấp các giá trị mặc định cho các trường khác
            val artistBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
            val genreIDsBody = "[]".toRequestBody("application/json".toMediaTypeOrNull())
            val songIDsBody = "[]".toRequestBody("application/json".toMediaTypeOrNull())
            val isPublicBody = "true".toRequestBody("text/plain".toMediaTypeOrNull())

            // Tạo một MultipartBody.Part rỗng cho ảnh bìa
            val emptyBody = "".toRequestBody("image/jpeg".toMediaTypeOrNull())
            val coverPart = MultipartBody.Part.createFormData("cover", "empty.jpg", emptyBody) // API của bạn có thể không cần cái này

            val token = SessionManager.getToken(requireContext())
            if (token.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Lỗi xác thực, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show()
                binding.btnCreate.isEnabled = true
                return@setOnClickListener
            }

            albumViewModel.createAlbum(
                "Bearer $token",
                titleBody, artistBody, genreIDsBody, descriptionBody, isPublicBody, songIDsBody,
                null // Truyền null cho ảnh bìa để tránh lỗi 500
            )
        }

        // Lắng nghe kết quả từ ViewModel
        albumViewModel.createAlbumResult.observe(viewLifecycleOwner) { result ->
            result?.onSuccess {
                Toast.makeText(requireContext(), "Tạo playlist thành công!", Toast.LENGTH_SHORT).show()
                // Tải lại danh sách album sau khi tạo thành công
                val token = SessionManager.getToken(requireContext())
                if (!token.isNullOrEmpty()) {
                    albumViewModel.getAllAlbums()
                }
                dismiss()
            }?.onFailure {
                Toast.makeText(requireContext(), "Tạo playlist thất bại: ${it.message}", Toast.LENGTH_SHORT).show()
            }
            binding.btnCreate.isEnabled = true // Kích hoạt lại nút
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        albumViewModel.createAlbumResult.postValue(null) // Dọn dẹp LiveData
        _binding = null
    }
}
