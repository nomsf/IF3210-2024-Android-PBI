package com.example.myapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.repository.TransactionRepository

class TransactionViewModel(context : Context) : ViewModel() {
    private val repository: TransactionRepository = TransactionRepository(context)
    var allTransactions : LiveData<List<TransactionEntity>> = repository.allTransactions

    var transactionObserver = Observer<List<TransactionEntity>> {
        // Nanti dipake di fragment yang ada object viewnya buat update itu
        // contohnya kaya gini (tapi harus ada recycle viewnya dulu
        // recyclerViewAdapter.submitList(it)

        //TODO("Implement kalau transaksi berubah di viewnya juga langsung berubah")
        Log.i("Development", "Transaction changed, transaction: $it")
    }
    fun addTransaction(title: String, nominal: String, kategori: String) {
        repository.insertTransaction(title, nominal , kategori)
    }

//    fun updateTransaction(transaction: TransactionEntity) {
//        transactionDao.update(transaction)
//    }

//    fun deleteTransaction(transaction: TransactionEntity) {
//        transactionDao.delete(transaction)
//    }
}