package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.ui.login.UserViewModel
import com.example.myapplication.util.LoginListener
import com.example.myapplication.util.SecretPreference

class LoginActivity : AppCompatActivity() , LoginListener {

    private lateinit var binding: ActivityLoginBinding
    private var userViewModel : UserViewModel = UserViewModel()
    private lateinit var secretPreference : SecretPreference
    private lateinit var authRepository : AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        secretPreference = SecretPreference(this)
        authRepository = AuthRepository(this, secretPreference)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val loginButton : Button = binding.loginButton

        loginButton.setOnClickListener {
            authRepository.loginRequest(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
            Log.d("Development", "Activity: Login request sent")
        }
    }

    override fun onLoginSuccess() {
        userViewModel.setUser(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
        Log.d("Development", "Activity: Login success")
        val preference : SharedPreferences = getSharedPreferences("secret_shared_prefs", MODE_PRIVATE)
        Log.d("Development", "Activity: Token: ${preference.getString("token", null)}\nPref: ${secretPreference.getToken()}")
        finish()
    }

    override fun onLoginFailure() {
        Log.d("Development", "Activity: Login failed")
    }


}