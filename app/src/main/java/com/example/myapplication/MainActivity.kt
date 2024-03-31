package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.service.TokenExpiryWorker
import com.example.myapplication.util.EventBus
import com.example.myapplication.util.SecretPreference
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var secretPreference : SecretPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val isOnline : String = if (isOnline()) "Online" else "Offline"
        Log.i("Development", "Online Connectivity Status: $isOnline")


        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)


    }

    override fun onStart() {
        super.onStart()

        // CHECK TOKEN for expiry
        val backgroundWork = PeriodicWorkRequestBuilder<TokenExpiryWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(backgroundWork)

        // event listener for token expiry
        EventBus.subscribe("TOKEN_EXPIRED") {
            Log.i("Development", "Token expired")
            secretPreference = SecretPreference(this)
            secretPreference.clearToken()
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

}