package com.example.nct_lite.ui.activity

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nct_lite.data.song.SongRepository
import com.example.nct_lite.databinding.UseruploadmusicBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserUploadMusicActivity : AppCompatActivity() {

    private lateinit var binding: UseruploadmusicBinding
    private var audioUri: Uri? = null
    private var coverUri: Uri? = null
    private val repository = SongRepository()

    private val audioPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            audioUri = uri
            binding.edtMp4.setText(uri?.let { getFileName(it) } ?: "")
        }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            coverUri = uri
            binding.edtCover.setText(uri?.let { getFileName(it) } ?: "")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UseruploadmusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnClear.setOnClickListener { clearFields() }
        binding.btnPickAudio.setOnClickListener { audioPicker.launch("audio/*") }
        binding.btnPickCover.setOnClickListener { imagePicker.launch("image/*") }
        binding.btnUpload.setOnClickListener { uploadSong() }
    }

    private fun clearFields() {
        binding.edtSongName.text?.clear()
        binding.edtAuthor.text?.clear()
        binding.edtCategory.text?.clear()
        binding.edtMp4.text?.clear()
        binding.edtCover.text?.clear()
        audioUri = null
        coverUri = null
    }

    private fun uploadSong() {
        val title = binding.edtSongName.text.toString().trim()
        val artist = binding.edtAuthor.text.toString().trim()
        val categoryInput = binding.edtCategory.text.toString().trim()
        val audio = audioUri
        val cover = coverUri
        val genreIds = categoryInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }

        if (title.isEmpty() || artist.isEmpty() || genreIds.isEmpty() || audio == null || cover == null) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn file", Toast.LENGTH_SHORT)
                .show()
            return
        }

        binding.btnUpload.isEnabled = false
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching {
                    val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
                    val artistBody = artist.toRequestBody("text/plain".toMediaTypeOrNull())
                    val genreJson = genreIds.joinToString(
                        prefix = "[\"",
                        postfix = "\"]",
                        separator = "\",\""
                    )
                    val genreBody = genreJson.toRequestBody("text/plain".toMediaTypeOrNull())
                    val songPart = createFilePart("song", audio)
                    val coverPart = createFilePart("cover", cover)

                    repository.uploadSong(titleBody, artistBody, genreBody, songPart, coverPart)
                }.getOrElse { Result.failure(it) }
            }
            binding.btnUpload.isEnabled = true
            result.onSuccess {
                Toast.makeText(this@UserUploadMusicActivity, "Upload thành công!", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure {
                Toast.makeText(this@UserUploadMusicActivity, it.message ?: "Upload thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val resolver = contentResolver
        val fileName = getFileName(uri) ?: "${partName}_${System.currentTimeMillis()}"
        val mimeType = resolver.getType(uri) ?: "application/octet-stream"
        val file = File(cacheDir, fileName)
        resolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) result = it.getString(index)
            }
        }
        return result
    }
}
