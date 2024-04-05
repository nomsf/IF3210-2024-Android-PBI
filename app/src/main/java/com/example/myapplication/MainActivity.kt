package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.service.CheckConnection
import com.example.myapplication.service.TokenExpiryWorker
import com.example.myapplication.util.EventBus
import com.example.myapplication.util.SecretPreference
import java.util.concurrent.TimeUnit
import com.example.myapplication.ui.settings.SettingsViewModel

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var secretPreference : SecretPreference
    private val checkConnection by lazy { CheckConnection(application) }
    private val connected : MutableLiveData<Boolean> = MutableLiveData(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        secretPreference = SecretPreference(this)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_list_transactions, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val loginIntent = Intent(this, LoginActivity::class.java)
//        startActivity(loginIntent)
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

        // CHECK TOKEN for expiry
        val backgroundWork = PeriodicWorkRequestBuilder<TokenExpiryWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(backgroundWork)

        // event listener for token expiry
        EventBus.subscribe("TOKEN_EXPIRED") {
            Log.i("Development", "Token expired")
            secretPreference = SecretPreference(this)
            secretPreference.clearSecretPreference()
            val loginIntent = Intent(this, LoginActivity::class.java)
            EventBus.eventClear("TOKEN_EXPIRED")
            startActivity(loginIntent)
        }

        val connectionLostBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        connectionLostBuilder
            .setMessage("Connection lost")
            .setTitle("Connection lost")

        val connectionLostDialog: AlertDialog = connectionLostBuilder.create()

        checkConnection.observe(this@MainActivity) {
            if (!it) {
                connectionLostDialog.show()
                updateConnection(false)
            } else {
                updateConnection(true)
            }
        }
    }
    fun updateConnection(value: Boolean) {
        connected.value = value
    }

    fun getConnectionStatus(): Boolean {
        return connected.value == true
    }

    fun getEmail(): String? {
        return secretPreference.getEmail()
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun logout() {
        secretPreference = SecretPreference(this)
        secretPreference.clearSecretPreference()
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
    }
}