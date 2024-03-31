package com.example.myapplication.backendconnect

import com.example.myapplication.model.auth.Token
import com.example.myapplication.model.auth.UserCred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface BackendService {
    @POST("auth/login")
    fun login(@Body userCred: UserCred): Call<Token>

    @POST("auth/token")
    fun tokenCheck(@Header("Authorization") token : String): Call<Token>

//    @POST("bill/upload")
//    fun uploadBill(@Body bill: Bill): Call<BillResponse>
}