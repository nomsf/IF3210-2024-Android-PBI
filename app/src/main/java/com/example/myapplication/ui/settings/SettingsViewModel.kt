package com.example.myapplication.ui.settings

import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import com.example.myapplication.NetworkConnection
import com.example.myapplication.ui.login.UserViewModel

class SettingsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text
}