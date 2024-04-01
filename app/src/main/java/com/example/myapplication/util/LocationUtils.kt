//package com.example.myapplication.util
//
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Bundle
//import androidx.core.content.ContextCompat
//
//object LocationUtils {
//
//    interface LocationCallback {
//        fun onLocationReceived(location: Location)
//    }
//
//    fun isLocationPermissionGranted(context: Context): Boolean {
//        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
//        val granted = PackageManager.PERMISSION_GRANTED
//        return ContextCompat.checkSelfPermission(context, permission) == granted
//    }
//
//    fun requestLocationUpdates(context: Context, callback: LocationCallback) {
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        val locationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location) {
//                callback.onLocationReceived(location)
//                locationManager.removeUpdates(this)
//            }
//
//            @Deprecated("Deprecated in Java")
//            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//
//            override fun onProviderEnabled(provider: String) {}
//
//            override fun onProviderDisabled(provider: String) {}
//        }
//
//        try {
//            val minTime: Long = 0
//            val minDistance = 0f
//            val criteria = LocationManager.PASSIVE_PROVIDER
//            @Suppress("DEPRECATION")
//            locationManager.requestSingleUpdate(criteria, locationListener, null)
//        } catch (e: SecurityException) {
//            e.printStackTrace()
//        }
//    }
//}