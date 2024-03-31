package com.example.myapplication.service

import android.content.Context
import android.util.Log
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