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
    @Insert
    fun insertTransaction(transaction: TransactionEntity)

    @Query("INSERT INTO transaction_entity (title, nominal, kategori, lokasi, tanggal) VALUES (:title, :nominal, :kategori, :lokasi, date('now'))")
    fun insertTransactionQuery(title: String, nominal: String, kategori: String, lokasi: String)

    @Query("SELECT * FROM transaction_entity")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Query("UPDATE transaction_entity SET title = :title, nominal = :nominal, kategori = :kategori, lokasi = :lokasi WHERE id = :id")
    fun update(title: String, nominal: String, kategori: String, lokasi: String, id: Int)

    @Query("DELETE FROM transaction_entity WHERE id = :id")
    fun delete(id : Int)
}