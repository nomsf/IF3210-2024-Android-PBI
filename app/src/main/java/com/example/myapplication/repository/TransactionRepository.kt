package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.TransactionEntity


class TransactionRepository(private val transactionDao: TransactionDao) {
    lateinit var allTransactions: MutableLiveData<List<TransactionEntity>>;

//    fun insertTransaction(transaction: TransactionEntity)
//    {
//        transactionDao.insertTransaction(transaction)
//    }
//
//    fun getAllTransactions()
//    {
//        this.allTransactions.postValue(transactionDao.getAllTransactions())
//    }
}