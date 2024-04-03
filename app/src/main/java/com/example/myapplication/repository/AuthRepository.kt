package com.example.myapplication.repository

import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapplication.backendconnect.Client
import com.example.myapplication.model.auth.Token
import com.example.myapplication.model.auth.UserCred
import com.example.myapplication.model.bill.BillResponse
import com.example.myapplication.util.EventBus
import com.example.myapplication.util.LoginListener
import com.example.myapplication.util.SecretPreference
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AuthRepository (
    private val secretPreference: SecretPreference) {

    fun loginRequest(email: String, password: String){
        Log.d("Development", "Login request to backend service")
        Client.connect.login(mapOf("email" to email, "password" to password)).enqueue(
            object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    Log.d("Development", "Response: ${response.body()}")
                    if (response.isSuccessful) {
                        val token : Token? = response.body()
                        Log.d("Development", "Successful Login\nToken: ${token?.token}")

                        // store the token ONLY, not the Token object
                        secretPreference.saveToken(token?.token ?: "")
                        secretPreference.saveEmail(email)

                        // callback the loginActivity
                        //loginListener.onLoginSuccess()
                        EventBus.publish("LOGIN_SUCCESS")

                    } else {
                        Log.d("Development", "LOGIN FAILED, http code : ${response.code()}")
                        //loginListener.onLoginFailure()
                        EventBus.publish("LOGIN_FAIL")
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.d("Development", "LOGIN FAILED, error on delivery : ${t.message}")
//                    loginListener.onLoginFailure()
                    EventBus.publish("LOGIN_FAIL")
                }
            }
        )
    }

    fun tokenCheckRequest() {
        Log.d("Development", "Token check request to backend service")
        val token = secretPreference.getToken() ?: ""

        Client.connect.tokenCheck("Bearer $token").enqueue(
            object : Callback<UserCred> {
                override fun onResponse(call: Call<UserCred>, response: Response<UserCred>) {
                    Log.d("Development", "Response: ${response.body()}")
                    if (response.isSuccessful) {
                        Log.d("Development", "Token check success: Valid token")
                    }
                    else if (response.code() == 401) {
                        Log.d("Development", "Token check failed: Invalid token or expired")
                        EventBus.publish("TOKEN_EXPIRED")
                    }
                    else {
                        Log.d("Development", "TOKEN CHECK FAILED, http code : ${response.code()}")
                        EventBus.publish("TOKEN_EXPIRED")
                    }
                }

                override fun onFailure(call: Call<UserCred>, t: Throwable) {
                    Log.d("Development", "TOKEN CHECK FAILED, error on delivery : ${t.message}")
                }
            }
        )
    }

    fun uploadBillRequest(file: File) {
        Log.d("Development", "Upload bill request to backend service")

        val token : String = secretPreference.getToken() ?: ""
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        Client.connect.uploadBill("Bearer $token", body).enqueue(
            object : Callback<BillResponse> {
                override fun onResponse(call: Call<BillResponse>, response: Response<BillResponse>) {
                    Log.d("Development", "Response: $response")
                    if (response.isSuccessful) {
                        Log.i("Development", "Upload Success, response: ${response.body()}")
                        EventBus.publish("UPLOAD_SUCCESS")
                    } else {
                        Log.d("Development", "UPLOAD FAILED, http code : ${response.code()}")
                        EventBus.publish("UPLOAD_FAIL")
                    }
                }

                override fun onFailure(call: Call<BillResponse>, t: Throwable) {
                    Log.d("Development", "UPLOAD FAILED, error on delivery : ${t.message}")
                    EventBus.publish("UPLOAD_FAIL")
                }
            }
        )
    }


}