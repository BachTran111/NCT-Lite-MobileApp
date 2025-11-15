package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.databinding.StartBinding
import com.example.nct_lite.util.ErrorDisplayHelper
import com.example.nct_lite.viewmodel.AuthViewModel

class StartActivity : AppCompatActivity() {

    private lateinit var binding: StartBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLoginResponse()

        binding.btnLogin.setOnClickListener {
            val username = binding.editUsername.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.login(username, password)
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeLoginResponse() {
        authViewModel.authResponse.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            result.onFailure { e ->
                val errorMessage = e.message ?: "Đăng nhập thất bại"
                // Log để debug
                android.util.Log.e("StartActivity", "Login failed: $errorMessage", e)
                
                // Nếu là lỗi kết nối (message chứa "không thể kết nối" hoặc dài), dùng Dialog
                if (errorMessage.contains("Không thể kết nối", ignoreCase = true) || 
                    errorMessage.length > 80) {
                    ErrorDisplayHelper.showConnectionError(this, errorMessage)
                } else {
                    // Lỗi ngắn thì dùng Toast
                    ErrorDisplayHelper.showErrorToast(this, errorMessage)
                }
            }
        }
    }
}
