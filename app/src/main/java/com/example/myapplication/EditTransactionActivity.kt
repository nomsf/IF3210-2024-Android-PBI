package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.database.TransactionDatabase
import com.example.myapplication.repository.TransactionRepository
import com.example.myapplication.ui.transactions.TransactionViewModel
import com.example.myapplication.ui.transactions.TransactionViewModelFactory
import android.widget.EditText
import android.widget.Button

import android.location.Location
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope


class EditTransactionActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var nominalEditText: EditText
    private lateinit var kategoriEditText: EditText
    private lateinit var lokasiEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionRepository: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        titleEditText = findViewById(R.id.editTextJudulEdit)
        nominalEditText = findViewById(R.id.editTextNominalEdit)
        kategoriEditText = findViewById(R.id.editTextKategoriEdit)
        lokasiEditText = findViewById(R.id.editTextLokasiEdit)
        saveButton = findViewById(R.id.buttonSimpanEdit)
        deleteButton = findViewById(R.id.deleteButton)

        val transactionDao = TransactionDatabase.getInstance(this).transactionDao()
        transactionRepository = TransactionRepository(this)
        transactionViewModel = ViewModelProvider(this, TransactionViewModelFactory(application)).get(TransactionViewModel::class.java)

        saveButton.setOnClickListener {
            lifecycleScope.launch {
//                saveEditTransaction()
            }
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
//                deleteTransaction()
            }
        }
    }

//    private suspend fun saveEditTransaction() {
//        val title = titleEditText.text.toString().trim()
//        val nominal = nominalEditText.text.toString().trim()
//        val kategori = kategoriEditText.text.toString().trim()
//        val lokasi = lokasiEditText.text.toString().trim()
//
//        if (title.isEmpty() || nominal.isEmpty() || kategori.isEmpty() || lokasi.isEmpty()) {
//            Toast.makeText(this, "Isi semua atribut transaksi", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        lifecycleScope.launch {
//            transactionViewModel.updateTransaction(title, nominal, kategori, lokasi)
//            Toast.makeText(this@EditTransactionActivity, "Transaksi berhasil diedit", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//        finish()
//    }

//    private suspend fun deleteTransaction() {
//        lifecycleScope.launch {
//            transactionViewModel.deleteTransaction()
//            Toast.makeText(this@EditTransactionActivity, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
//            finish()
//        }
//        finish()
//    }
}