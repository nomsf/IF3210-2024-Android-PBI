package com.example.myapplication.database

import com.example.myapplication.dao.TransactionDao
import com.example.myapplication.entities.TransactionEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.converters.DateConverter
import androidx.room.TypeConverters


@Database (entities = [TransactionEntity::class], version = 1, exportSchema = false)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var instance: TransactionDatabase? = null

        fun getInstance (context: Context): TransactionDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java,
                        "transaction_database"
                ).build().also { instance = it }
            }
        }
    }
}