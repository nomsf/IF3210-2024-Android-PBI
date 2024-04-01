package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.TransactionEntity


class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: LiveData<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return transactionDao.insertTransaction(transaction)
    }
}