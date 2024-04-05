# IF3210-2024-Android-PBI

## Deskripsi Aplikasi
<p align="justify"> BondoMan adalah sebuah aplikasi berbasis Android untuk manajemen pengeluaran dan pemasukan. Untuk masuk ke dalam aplikasi, pengguna harus melakukan <i>login</i> terlebih dahulu.</p>
<p align="justify"> Kemudian, pengguna dapat melihat daftar transaksi yang telah dilakukan, juga menambah transaksi. Selain itu, pengguna dapat mengedit atau menghapus transaksi yang telah dilakukan dengan cara memlihnya dan masuk ke halaman pengeditan.</p>
<p align="justify"> Pengguna dapat melihat diagram lingkaran transaksi yang terbentuk berdasarkan jumlah kategori: "Pengeluaran" atau "Pemasukan".</p>
<p align="justify"> Terdapat fitur <i>scan</i> nota menggunakan kamera, kemudian nota akan dikirim ke server. Tersedia pula tempat untuk mengirim informasi transaksi melalui email. Melalui halaman <i>setting</i>, pengguna dapat mengunduh daftar transaksi yang telah dilakukan, ke dalam format fail .xlxs</p>
<p align="justify"> Terakhir, pengguna dapat melakukan penambahan secara random, karena aplikasi dilengkapi dengan <i>broadcast receiver</i></p>

## Daftar <i>Library</i>
Berikut adalah daftar <i>library</i> yang digunakan untuk pengembangan Bondo Man:
- androidx.core:core-ktx
- androidx.appcompat:appcompat
- com.google.android.material:material
- androidx.constraintlayout:constraintlayout
- androidx.lifecycle:lifecycle-livedata-ktx
- androidx.lifecycle:lifecycle-viewmodel-ktx
- androidx.navigation:navigation-fragment-ktx
- androidx.navigation:navigation-ui-ktx
- pl.droidsonroids.gif:android-gif-drawable
- io.reactivex.rxjava3:rxjava
- io.reactivex.rxjava3:rxandroid
- com.squareup.retrofit2:retrofit
- com.squareup.retrofit2:converter-gson
- androidx.security:security-crypto
- org.jetbrains.kotlinx:kotlinx-coroutines-android
- androidx.work:work-runtime-ktx
- androidx.compose.runtime:runtime-livedata
- androidx.compose.runtime:runtime-rxjava2
- junit:junit
- androidx.test:core
- androidx.test.espresso:espresso-core
- androidx.room:room-runtime
- androidx.room:room-compiler
- androidx.room:room-ktx
- androidx.room:room-testing

## Pembagian Kerja

|NIM             |Nama                           |Pembagian Kerja                                                                                               |
|----------------|-------------------------------|--------------------------------------------------------------------------------------------------------------|
|10023457		 |Habibi Galang Trianda			 |Header dan Navbar, Menyimpan Daftar Transaksi dalam Format .xlsx, .xls                                        |
|13521050		 |Naufal Syifa Firdaus           |Header dan Navbar, Login, Logout, Melakukan Scan Nota, Mengecek expiry JWT, Broadcast Receiver                |
|13521069        |Louis Caesa Kesuma           	 |Header dan Navbar, Logout, Graf Rangkuman Transaksi, Intent GMail, Network Sensing, Broadcast Receiver        |
|13521140        |Ryan Samuel Chandra			 |Melakukan Penambahan, Pengubahan, dan Penghapusan Transaksi, Melihat Daftar Transaksi yang Sudah Dilakukan    |

## Jumlah Jam Pengerjaan


|NIM             |Nama                           |Total Waktu Kerja                  |
|----------------|-------------------------------|-----------------------------------|
|10023457		 |Habibi Galang Trianda			 |Persiapan 24 jam, Pengerjaan 24 jam|
|13521050		 |Naufal Syifa Firdaus           |Persiapan 10 jam, Pengerjaan 48 jam|
|13521069        |Louis Caesa Kesuma           	 |Persiapan 14 jam, Pengerjaan 48 jam|
|13521140        |Ryan Samuel Chandra			 |Persiapan 15 jam, Pengerjaan 48 jam|