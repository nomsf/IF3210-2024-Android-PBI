package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.TransactionDatabase
import com.example.myapplication.entities.TransactionEntity
import com.example.myapplication.repository.TransactionRepository
import com.example.myapplication.viewmodel.TransactionViewModel
import java.util.Date
import android.widget.EditText
import android.widget.Button

import android.location.Location
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import android.annotation.SuppressLint
//import com.example.myapplication.util.LocationUtils
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager


class AddTransactionActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var nominalEditText: EditText
    private lateinit var kategoriEditText: EditText
    private lateinit var lokasiEditText: EditText
    private lateinit var saveButton: Button

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var transactionRepository: TransactionRepository

    private var currentLocation: Location? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_transaction)

        titleEditText = findViewById(R.id.editTextJudul)
        nominalEditText = findViewById(R.id.editTextNominal)
        kategoriEditText = findViewById(R.id.editTextKategori)
        lokasiEditText = findViewById(R.id.editTextLokasi)
        saveButton = findViewById(R.id.buttonSimpan)

        val transactionDao = TransactionDatabase.getInstance(this).transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveTransaction()
            }
        }
//        getLocation()
    }

    private suspend fun saveTransaction() {
        val title = titleEditText.text.toString().trim()
        val nominal = nominalEditText.text.toString().trim()
        val kategori = kategoriEditText.text.toString().trim()
        val lokasi = lokasiEditText.text.toString().trim()
        val tanggal = Date()

        if (title.isEmpty() || nominal.isEmpty() || kategori.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(this, "Isi semua atribut transaksi", Toast.LENGTH_SHORT).show()
            return
        }

        val transaction = TransactionEntity(1, title, nominal, kategori, lokasi, tanggal)
        lifecycleScope.launch {
            transactionViewModel.addTransaction(transaction)
            Toast.makeText(this@AddTransactionActivity, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            finish()
        }
        finish()
    }


//    @SuppressLint("MissingPermission")
//    private fun getLocation() {
//        if (LocationUtils.isLocationPermissionGranted(this)) {
//            LocationUtils.requestLocationUpdates(this, object : LocationUtils.LocationCallback {
//                override fun onLocationReceived(location: Location) {
//                    currentLocation = location
//                    lokasiEditText.setText("${location.latitude}, ${location.longitude}")
//                }
//            })
//        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLocation()
//            } else {
//                Toast.makeText(this, "Izin akses lokasi ditolak", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}