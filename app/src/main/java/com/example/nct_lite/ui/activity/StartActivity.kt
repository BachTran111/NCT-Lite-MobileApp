package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nct_lite.databinding.StartBinding
import com.example.nct_lite.viewmodel.auth.AuthViewModel
import com.example.nct_lite.MyApplication
import com.example.nct_lite.viewmodel.auth.AuthViewModelFactory
import com.example.nct_lite.data.auth.AuthRepository
class StartActivity : AppCompatActivity() {

    private lateinit var binding: StartBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy repository từ AppContainer trong Application
        val app = application as MyApplication
        val factory = AuthViewModelFactory(app.appContainer.authRepository)

        // Tạo ViewModel bằng factory
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

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
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun observeLoginResponse() {
        authViewModel.authResponse.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            result.onFailure {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
