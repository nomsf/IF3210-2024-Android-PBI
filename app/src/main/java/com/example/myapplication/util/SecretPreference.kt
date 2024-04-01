package com.example.myapplication.util

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import android.content.Context
import android.util.Log

class SecretPreference (private val context: Context) {

    private val  masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    fun saveToken(token: String){
        sharedPreferences.edit().putString("token", token).apply()
        Log.i("Development", "Token saved")
    }

    fun getToken(): String? = sharedPreferences.getString("token", null)

}