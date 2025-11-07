package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.databinding.StartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: StartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Nút "Log in"
        binding.btnLogin.setOnClickListener {
            val username = binding.editUsername.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            // Kiểm tra rỗng
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Giả lập xác thực tài khoản
            if (username == "admin" && password == "1234") {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                // Điều hướng sang MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // kết thúc StartActivity để không quay lại được
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 Nút "Sign up free"
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}