package com.example.myapplication.ui.transactions

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.repository.TransactionRepository

class TransactionViewModel(application : Application) : ViewModel() {
    private val repository: TransactionRepository = TransactionRepository(application)
    var allTransactions : LiveData<List<TransactionEntity>> = repository.allTransactions
//
//    var transactionObserver = Observer<List<TransactionEntity>> {
//        // Nanti dipake di fragment yang ada object viewnya buat update itu
//        // contohnya kaya gini (tapi harus ada recycle viewnya dulu
//        // recyclerViewAdapter.submitList(it)
//
//        //TODO("Implement kalau transaksi berubah di viewnya juga langsung berubah")
//        Log.i("Development", "Transaction changed, transaction: $it")
//    }
    suspend fun addTransaction(title: String, nominal: String, kategori: String, lokasi: String, tanggal: String) {
        repository.insertTransaction(title, nominal , kategori, lokasi, tanggal)
    }

    suspend fun updateTransaction() {
        TODO("implement update transaction")
    }

    suspend fun deleteTransaction() {
        TODO("implement delete transaction")
    }

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text
}