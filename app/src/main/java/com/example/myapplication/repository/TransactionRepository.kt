package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.database.TransactionDatabase
import com.example.myapplication.entities.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TransactionRepository(context: Context) {
    var allTransactions: LiveData<List<TransactionEntity>>;
    private val transactionDao : TransactionDao

    init {
        val transactionDatabase = TransactionDatabase.getInstance(context)
        transactionDao = transactionDatabase.transactionDao()
        allTransactions = transactionDao.getAllTransactions()
    }

    suspend fun insertTransaction(title: String, nominal: String, kategori: String, lokasi: String){
        withContext(Dispatchers.IO) {
            val transaction = TransactionEntity(
                title = title,
                nominal = nominal,
                kategori = kategori,
                lokasi = lokasi,
                tanggal = null
            )
            transactionDao.insertTransaction(transaction)
        }
    }

    suspend fun insertTransactionQuery(title: String, nominal: String, kategori: String, lokasi: String) {
        withContext(Dispatchers.IO) {
            transactionDao.insertTransactionQuery(title, nominal, kategori, lokasi)
        }
    }

    suspend fun updateTransaction(title: String, nominal: String, kategori: String, lokasi: String, id: Int) {
        withContext(Dispatchers.IO) {
            // id buat yang mau diupdate data yang mana
            transactionDao.update(title, nominal, kategori, lokasi, id)
        }
    }

    suspend fun deleteTransaction(id: Int) {
        withContext(Dispatchers.IO) {
            transactionDao.delete(id)
        }
    }
}