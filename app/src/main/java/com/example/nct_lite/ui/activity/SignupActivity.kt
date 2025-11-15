package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.R
import com.example.nct_lite.util.ErrorDisplayHelper
import com.example.nct_lite.viewmodel.AuthViewModel

class SignupActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnCreate = findViewById<Button>(R.id.btn_create_account)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etName = findViewById<EditText>(R.id.et_name)

        observeRegisterResponse()

        btnBack.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        btnCreate.setOnClickListener {
            val username = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val name = etName.text.toString().trim()  // optional, backend không dùng

            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.register(username, password)
        }
    }

    private fun observeRegisterResponse() {
        authViewModel.authResponse.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ChooseArtistsActivity::class.java)
                intent.putExtra("USER_NAME", it.metadata.role)
                startActivity(intent)
                finish()
            }

            result.onFailure { e ->
                val errorMessage = e.message ?: "Đăng ký thất bại"
                // Log để debug
                android.util.Log.e("SignupActivity", "Register failed: $errorMessage", e)
                
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
