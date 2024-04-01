package com.example.myapplication.service

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.util.LoginListener
import com.example.myapplication.util.SecretPreference

class TokenExpiryWorker (appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    private val secretPreference = SecretPreference(applicationContext)
    private val authRepository = AuthRepository(secretPreference)

    override suspend fun doWork(): Result {
        Log.i("Development", "Token expiry worker started")
        authRepository.tokenCheckRequest()
        return Result.success()
    }
}

//class TokenExpiryWorker(private val context: Context) : LiveData<Boolean>() {
//
//    private val secretPreference = SecretPreference(context)
//    private val authRepository = AuthRepository(secretPreference)
//
//    override fun onActive() {
//        super.onActive()
//        val isTokenExpired = checkTokenExpiry()
//        authRepository.tokenCheckRequest()
//        postValue(isTokenExpired)
//    }
//
//    private fun checkTokenExpiry(): Boolean {
//        val tokenExpiryTime = tokenExpiryService.getTokenExpiryTime()
//        val currentTime = Date().time
//        return currentTime > tokenExpiryTime
//    }
//}