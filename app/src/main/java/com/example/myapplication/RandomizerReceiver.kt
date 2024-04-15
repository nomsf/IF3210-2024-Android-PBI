package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RandomizerReceiver : BroadcastReceiver() {
    private fun generateRandomTitle(): String {
        val titles = listOf("Title 1", "Title 2", "Title 3")
        val randomIndex = (0 until titles.size).random()
        return titles[randomIndex]
    }

    private fun generateRandomNominal(): Int {
        return (0..100).random()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.myapplication.ui.transactions.RANDOMIZE") {
            val intent = Intent(context, TransactionActivity::class.java)

            val randomTitle = generateRandomTitle()
            val randomNominal = generateRandomNominal()

            intent.putExtra("title", randomTitle)
            intent.putExtra("nominal", randomNominal)

            context!!.startActivity(intent)
        } else {
            Log.d("Invalid ACTION", "Failed to receive broadcast")
        }
    }
}