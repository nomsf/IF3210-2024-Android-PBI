package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.TransactionEntity

class TransactionViewModel(private val transactionDao: TransactionDao) : ViewModel() {


    fun addTransaction(transaction: TransactionEntity) {

    }

    fun getAllTransactions() = transactionDao.getAllTransactions()

    fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.update(transaction)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }
}