package com.example.nct_lite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Ánh xạ view
        val btnBack = findViewById<ImageButton?>(R.id.btn_back)
        val btnCreate = findViewById<Button?>(R.id.btn_create_account)
        val etEmail = findViewById<EditText?>(R.id.et_email)
        val etPassword = findViewById<EditText?>(R.id.et_password)
        val etName = findViewById<EditText?>(R.id.et_name)

        // ✅ Phòng tránh lỗi null
        if (btnBack == null || btnCreate == null) {
            Toast.makeText(this, "Layout error: Missing button IDs", Toast.LENGTH_SHORT).show()
            return
        }

        // Nút quay lại → StartActivity
        btnBack.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Nút tạo tài khoản
        btnCreate.setOnClickListener {
            val email = etEmail?.text?.toString()?.trim() ?: ""
            val password = etPassword?.text?.toString()?.trim() ?: ""
            val name = etName?.text?.toString()?.trim() ?: ""

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()

            // 👉 Chuyển sang ChooseArtistsActivity thay vì MainActivity
            val intent = Intent(this, ChooseArtistsActivity::class.java)
            intent.putExtra("USER_NAME", name)
            startActivity(intent)
            finish()
        }
    }
}
