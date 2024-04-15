package com.example.myapplication

import android.content.IntentFilter
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

//import android.location.Location
import android.text.TextUtils
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Geocoder


class TransactionActivity : AppCompatActivity() {
    private val randomizerReceiver = RandomizerReceiver()
    private lateinit var titleEditText: EditText
    private lateinit var nominalEditText: EditText
    private lateinit var kategoriEditText: EditText
    private lateinit var lokasiEditText: EditText
    private lateinit var saveButton: Button

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionRepository: TransactionRepository

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(randomizerReceiver, IntentFilter("com.example.myapplication.ui.transactions.RANDOMIZE"))
        setContentView(R.layout.activity_add_transaction)

        titleEditText = findViewById(R.id.editTextJudul)
        nominalEditText = findViewById(R.id.editTextNominal)
        kategoriEditText = findViewById(R.id.editTextKategori)
        lokasiEditText = findViewById(R.id.editTextLokasi)
        saveButton = findViewById(R.id.buttonSimpan)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        val transactionDao = TransactionDatabase.getInstance(this).transactionDao()
        transactionRepository = TransactionRepository(this)
        transactionViewModel = ViewModelProvider(this, TransactionViewModelFactory(application)).get(TransactionViewModel::class.java)

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveTransaction()
            }
        }
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                val geocoder = Geocoder(this)
                try {
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses?.isNotEmpty() == true) {
                        val address = addresses[0]
                        val addressLine = address.getAddressLine(0)
                        lokasiEditText.setText(addressLine)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun saveTransaction() {
        val title = titleEditText.text.toString().trim()
        val nominal = nominalEditText.text.toString().trim()
        val kategori = kategoriEditText.text.toString().trim()
        val lokasi = lokasiEditText.text.toString().trim()

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
            transactionViewModel.addTransaction(title, nominal, kategori, lokasi)
            Toast.makeText(this@TransactionActivity, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            finish()
        }
        finish()
    }

    override fun onDestroy() {
        unregisterReceiver(randomizerReceiver);
        super.onDestroy()
    }
}