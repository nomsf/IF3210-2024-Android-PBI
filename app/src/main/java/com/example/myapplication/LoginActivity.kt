package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.ui.login.UserViewModel
import com.example.myapplication.util.EventBus
import com.example.myapplication.util.LoginListener
import com.example.myapplication.util.SecretPreference
import com.example.myapplication.NetworkConnection

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var userViewModel : UserViewModel = UserViewModel()
    private lateinit var secretPreference : SecretPreference
    private lateinit var authRepository : AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        secretPreference = SecretPreference(this)
        authRepository = AuthRepository(secretPreference)

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

        EventBus.subscribe("LOGIN_SUCCESS") {
            onLoginSuccess()
        }

        EventBus.subscribe("LOGIN_FAIL") {
            onLoginFailure()
        }
    }

    fun onLoginSuccess() {
        userViewModel.setUser(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
        Log.d("Development", "Activity: Login success")
        val preference : SharedPreferences = getSharedPreferences("secret_shared_prefs", MODE_PRIVATE)
        Log.d("Development", "Activity: Token: ${preference.getString("token", null)}\nPref: ${secretPreference.getToken()}")

        val resultIntent = Intent()
        resultIntent.putExtra("userData", binding.emailInput.text.toString())
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    fun onLoginFailure() {
        Log.d("Development", "Activity: Login failed")
    }
}