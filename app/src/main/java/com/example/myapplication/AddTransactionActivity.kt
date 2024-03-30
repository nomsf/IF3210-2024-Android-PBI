import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import java.util.Date


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
        setContentView(R.layout.activity_add_transaction)

        titleEditText = findViewById(R.id.editTextJudul)
        nominalEditText = findViewById(R.id.editTextNominal)
        kategoriEditText = findViewById(R.id.editTextKategori)
        lokasiEditText = findViewById(R.id.editTextLokasi)
        saveButton = findViewById(R.id.buttonSimpan)

        val transactionDao = TransactionDatabase.getInstance(this).transactionDao()
        transactionRepository = TransactionRepository(transactionDao)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        transactionViewModel.initialize(transactionRepository)

        saveButton.setOnClickListener { saveTransaction() }
        getLocation()
    }

    private fun saveTransaction() {
        val title = titleEditText.text.toString().trim()
        val nominal = nominalEditText.text.toString().trim()
        val kategori = kategoriEditText.text.toString().trim()
        val lokasi = lokasiEditText.text.toString().trim()
        val tanggal = Date()

        if (title.isEmpty() || nominal.isEmpty() || kategori.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(this, "Isi semua atribut transaksi", Toast.LENGTH_SHORT).show()
            return
        }

        val transaction = TransactionEntity(title, nominal, kategori, lokasi, tanggal)
        transactionViewModel.addTransaction(transaction)
        Toast.makeText(this, "Transaksi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
        finish()
    }

    
    private fun saveTransaction() {
        val title = titleEditText.text.toString().trim()
        val nominal = nominalEditText.text.toString().trim()
        val kategori = kategoriEditText.text.toString().trim()
        val lokasi = lokasiEditText.text.toString().trim()
        val tanggal = Date()

        if (title.isEmpty() || nominal.isEmpty() || kategori.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(this, "Isi semua atribut transaksi", Toast.LENGTH_SHORT).show()
            return
        }

        val transaction = TransactionEntity(title = title, nominal = nominal, kategori = kategori, lokasi = lokasi, tanggal = tanggal)
        val insertedTransactionId = transactionViewModel.addTransaction(transaction)
        Toast.makeText(this, "Transaksi berhasil ditambahkan dengan ID $insertedTransactionId", Toast.LENGTH_SHORT).show()
        finish()
    }

    
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (LocationUtils.isLocationPermissionGranted(this)) {
            LocationUtils.requestLocationUpdates(this) { location ->
                currentLocation = location
                lokasiEditText.setText("${location.latitude}, ${location.longitude}")
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Izin akses lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }
}