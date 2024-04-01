package com.example.myapplication.backendconnect

import com.example.myapplication.model.auth.Token
import com.example.myapplication.model.auth.UserCred
import com.example.myapplication.model.bill.BillResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface BackendService {
    @POST("auth/login")
    fun login(@Body data: Map<String, String>): Call<Token>

    @POST("auth/token")
    fun tokenCheck(@Header("Authorization") token : String): Call<UserCred>

    @Multipart
    @POST("bill/upload")
    fun uploadBill(@Header("Authorization") token: String , @Part file: MultipartBody.Part): Call<BillResponse>
}