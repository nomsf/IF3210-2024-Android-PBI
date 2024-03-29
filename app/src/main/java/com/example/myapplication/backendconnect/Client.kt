package com.example.myapplication.backendconnect

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object Client {
    private const val BASE_URL = "https://pbd-backend-2024.vercel.app/api/"

    val connect : BackendService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(BackendService::class.java)
    }
}