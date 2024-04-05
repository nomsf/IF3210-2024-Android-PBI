package com.example.myapplication.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "transaction_entity")
data class TransactionEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Int = 0,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "nominal")
        val nominal: String,

        @ColumnInfo(name = "kategori")
        val kategori: String,

        @ColumnInfo(name = "lokasi", defaultValue = "NULL")
        val lokasi: String?,

        @ColumnInfo(name = "tanggal", defaultValue = "CURRENT_DATE")
        val tanggal: String?
)