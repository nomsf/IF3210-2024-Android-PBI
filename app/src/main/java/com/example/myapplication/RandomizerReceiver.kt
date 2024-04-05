package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RandomizerReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "RANDOMIZE") {
            Log.d("TESTING", "Received a proper broadcast")
        } else {
            Log.d("Invalid ACTION", "Failed to receive broadcast")
        }
    }
}