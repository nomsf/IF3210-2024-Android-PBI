package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.ui.login.UserViewModel
import com.example.myapplication.util.LoginListener

class LoginActivity : AppCompatActivity() , LoginListener {

    private lateinit var binding: ActivityLoginBinding
    private var userViewModel : UserViewModel = UserViewModel()
    private var authRepository : AuthRepository = AuthRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        finish()
    }

    override fun onLoginFailure() {
        Log.d("Development", "Activity: Login failed")
    }


}