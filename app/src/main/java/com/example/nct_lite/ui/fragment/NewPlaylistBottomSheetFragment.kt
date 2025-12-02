package com.example.nct_lite.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.nct_lite.data.album.AlbumRepository
import com.example.nct_lite.databinding.BottomSheetNewPlaylistBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NewPlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetNewPlaylistBinding? = null
    private val binding get() = _binding!!

    // Biến lưu Uri ảnh được chọn
    private var coverUri: Uri? = null

    private val albumRepository = AlbumRepository()

    // 1. Sửa Image Picker
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            coverUri = uri
            // Hiển thị tên file lên nút bấm hoặc TextView
            binding.btnAddImage.text = uri?.let { getFileName(it) } ?: "Chọn ảnh bìa"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener { dismiss() }

        binding.btnCreate.setOnClickListener { createAlbum() }

        // 2. Sửa sự kiện click chọn ảnh
        binding.btnAddImage.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }

    private fun createAlbum() {
        val title = binding.editPlaylistName.text.toString().trim()
        val description = binding.editDescription.text.toString().trim()
        val isPublic = binding.cbPublic.isChecked

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnCreate.isEnabled = false

        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    val titleRB = title.toRequestBody("text/plain".toMediaTypeOrNull())
                    val descRB = if (description.isNotEmpty()) {
                        description.toRequestBody("text/plain".toMediaTypeOrNull())
                    } else null
                    val publicRB = isPublic.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    var coverPart: MultipartBody.Part? = null
                    if (coverUri != null) {
                        coverPart = createFilePart("cover", coverUri!!)
                    }
                    albumRepository.createAlbum(titleRB, descRB, publicRB, coverPart)

                }.getOrElse {
                    Result.failure(it)
                }
            }

            binding.btnCreate.isEnabled = true

            result.onSuccess { response ->
                Toast.makeText(requireContext(), "Success to create playlist '${response.metadata.title}'!", Toast.LENGTH_SHORT).show()
                dismiss()
            }.onFailure { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val context = requireContext()
        val resolver = context.contentResolver

        val fileName = getFileName(uri) ?: "${partName}_${System.currentTimeMillis()}"
        val mimeType = resolver.getType(uri) ?: "image/*"

        val file = File(context.cacheDir, fileName)

        resolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }

        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) result = it.getString(index)
            }
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}