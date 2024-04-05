package com.example.myapplication.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.repository.TransactionRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppBroadcastReceiver : BroadcastReceiver(){
    private lateinit var transactionRepository : TransactionRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        val map: Map<String, String>? = intent?.getSerializableExtra("map") as? Map<String, String>
        transactionRepository = TransactionRepository(context!!)
        GlobalScope.launch {
            insertToDatabase(map!!, transactionRepository)
        }
    }

    suspend fun insertToDatabase(map : Map<String, String>, transactionRepository: TransactionRepository){
        transactionRepository.insertTransactionQuery(
            title= map.get("title")!!,
            nominal = map["nominal"]!!.toString(),
            kategori = "Randomize",
            lokasi = "Unknown"
        )
    }
}