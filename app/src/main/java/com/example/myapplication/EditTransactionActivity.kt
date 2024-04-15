package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.TransactionDatabase
import com.example.myapplication.repository.TransactionRepository
import com.example.myapplication.ui.transactions.TransactionViewModel
import com.example.myapplication.ui.transactions.TransactionViewModelFactory
import android.widget.EditText
import android.widget.Button

import android.location.Location
import android.widget.ImageButton
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import android.content.Intent
import android.text.TextUtils


class EditTransactionActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var nominalEditText: EditText
    private lateinit var kategoriEditText: EditText
    private lateinit var lokasiEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: ImageButton

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

        val bundle: Bundle? = intent.extras
        val title = bundle!!.getString("title")
        val nominal = bundle.getString("nominal")
        val kategori = bundle.getString("kategori")
        val lokasi = bundle.getString("lokasi")

        titleEditText.setText(title)
        nominalEditText.setText(nominal)
        kategoriEditText.setText(kategori)
        lokasiEditText.setText(lokasi)


        val transactionDao = TransactionDatabase.getInstance(this).transactionDao()
        transactionRepository = TransactionRepository(this)
        transactionViewModel = ViewModelProvider(this, TransactionViewModelFactory(application)).get(TransactionViewModel::class.java)

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveEditTransaction()
            }
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                deleteTransaction()
            }
        }
    }

    private suspend fun saveEditTransaction() {
        val bundle: Bundle? = intent.extras
        val id = bundle!!.getInt("id")

        val title = titleEditText.text.toString().trim()
        val nominal = nominalEditText.text.toString().trim()
        val kategori = kategoriEditText.text.toString().trim()
        val lokasi = lokasiEditText.text.toString().trim()

        if (title.isEmpty() || nominal.isEmpty() || kategori.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(this, "Isi semua atribut transaksi", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty() || nominal.isEmpty() || kategori.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(this, "Isi semua atribut transaksi", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.length > 25) {
            Toast.makeText(this, "Judul terlalu panjang", Toast.LENGTH_SHORT).show()
            return
        }

        if (!TextUtils.isDigitsOnly(nominal)) {
            Toast.makeText(this, "Nominal harus berupa angka. Tanpa spasi, koma, dan titik.", Toast.LENGTH_SHORT).show()
            return
        }

        if (nominal.length > 12) {
            Toast.makeText(this, "Nominal terlalu panjang", Toast.LENGTH_SHORT).show()
            return
        }

        if (kategori != "Pemasukan" && kategori != "Pengeluaran") {
            Toast.makeText(this, "Kategori harus \"Pemasukan\" atau \"Pengeluaran\"", Toast.LENGTH_SHORT).show()
            return
        }

        if (lokasi.length > 17) {
            Toast.makeText(this, "Lokasi terlalu panjang", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            transactionViewModel.updateTransaction(title, nominal, kategori, lokasi, id)
            Toast.makeText(this@EditTransactionActivity, "Transaksi berhasil diedit", Toast.LENGTH_SHORT).show()
            finish()
        }
        finish()
    }

    private suspend fun deleteTransaction() {
        val bundle: Bundle? = intent.extras
        val id = bundle!!.getInt("id")

        lifecycleScope.launch {
            transactionViewModel.deleteTransaction(id)
            Toast.makeText(this@EditTransactionActivity, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@EditTransactionActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        finish()
    }
}