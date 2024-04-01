package com.example.myapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.myapplication.entities.TransactionEntity


@Dao
interface TransactionDao {
    @Query("INSERT INTO `" + "transactionentity" + "`(title) VALUES (:title)")
    fun insertTransaction(title: String)

    @Query("SELECT * FROM transactionentity")
    fun getAllTransactions(): List<TransactionEntity>

    @Update
    fun update(transaction: TransactionEntity)

    @Delete
    fun delete(transaction: TransactionEntity)
}