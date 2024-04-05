package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.database.TransactionDatabase
import com.example.myapplication.entities.TransactionEntity


class TransactionRepository(context: Context) {
    var allTransactions: LiveData<List<TransactionEntity>>;
    private val transactionDao : TransactionDao

    init {
        val transactionDatabase = TransactionDatabase.getInstance(context)
        transactionDao = transactionDatabase.transactionDao()
        allTransactions = transactionDao.getAllTransactions()
    }

    fun insertTransaction(title: String, nominal: String, kategori: String, lokasi: String, tanggal: String) {
        val transaction = TransactionEntity(title = title, nominal = nominal, kategori = kategori, lokasi = lokasi, tanggal = tanggal)
        transactionDao.insertTransaction(transaction)
    }

    fun updateTransaction(title: String, nominal: String, kategori: String, id: Int) {
        // id buat yang mau diupdate data yang mana
        transactionDao.update(title, nominal, kategori, id)
    }

    fun deleteTransaction(id: Int) {
        transactionDao.delete(id)
    }
}