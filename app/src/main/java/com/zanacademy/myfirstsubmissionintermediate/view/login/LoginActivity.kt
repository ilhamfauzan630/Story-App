package com.zanacademy.myfirstsubmissionintermediate.view.login

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.zanacademy.myfirstsubmissionintermediate.data.ResultState
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserModel
import com.zanacademy.myfirstsubmissionintermediate.data.pref.UserPreference
import com.zanacademy.myfirstsubmissionintermediate.data.pref.dataStore
import com.zanacademy.myfirstsubmissionintermediate.databinding.ActivityLoginBinding
import com.zanacademy.myfirstsubmissionintermediate.view.ViewModelFactory
import com.zanacademy.myfirstsubmissionintermediate.view.main.MainActivity
import com.zanacademy.myfirstsubmissionintermediate.view.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.userLogin(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }
                        is ResultState.Success -> {
                            val message = result.data.message
                            alertDialog(message)

                            val token = result.data.loginResult?.token

                            if (token != null) {
                                viewModel.saveSession(UserModel(email, token))
                            }

                            showLoading(false)
                        }
                        is ResultState.Error -> {
                            val error = result.error
                            showToast(error)
                            showLoading(false)

                            startActivity(Intent(this, WelcomeActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun alertDialog(message: String?) {
        AlertDialog.Builder(this@LoginActivity).apply {
            setTitle("Yeah!")
            setMessage(message)
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }
}