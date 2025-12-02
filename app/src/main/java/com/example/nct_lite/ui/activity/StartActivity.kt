package com.example.nct_lite.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nct_lite.MyApplication
import com.example.nct_lite.databinding.StartBinding
import com.example.nct_lite.viewmodel.auth.AuthViewModel
import com.example.nct_lite.viewmodel.auth.AuthViewModelFactory

class StartActivity : AppCompatActivity() {

    private lateinit var binding: StartBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory((application as MyApplication).appContainer.authRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val savedToken = com.example.nct_lite.data.SessionManager.getToken(this)
        val savedRole = com.example.nct_lite.data.SessionManager.getRole(this)
        if (!savedToken.isNullOrEmpty() && !savedRole.isNullOrEmpty()) {
            com.example.nct_lite.data.ApiClient.authToken = savedToken
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    putExtra("USER_ROLE", savedRole)
                }
            )
            finish()
            return
        }

//        binding.editUsername.setText("admin")
//        binding.editPassword.setText("admin")

        observeLoginResponse()

        binding.btnLogin.setOnClickListener {
            val username = binding.editUsername.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
                com.example.nct_lite.data.SessionManager.saveAuth(
                    this,
                    it.metadata.token,
                    it.metadata.role
                )
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("USER_ROLE", it.metadata.role)
                }
                startActivity(intent)
                finish()
            }

            result.onFailure { e ->
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
