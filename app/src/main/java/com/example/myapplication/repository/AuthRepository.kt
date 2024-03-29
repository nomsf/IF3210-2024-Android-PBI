package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.backendconnect.Client
import com.example.myapplication.model.auth.Token
import com.example.myapplication.model.auth.UserCred
import com.example.myapplication.util.LoginListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository (private val loginListener: LoginListener) {

    fun loginRequest(email: String, password: String){
        Log.d("Development", "Login request to backend service")
        Client.connect.login(UserCred(email, password)).enqueue(
            object : Callback<Token> {
                override fun onResponse(call: Call<Token>, response: Response<Token>) {
                    Log.d("Development", "Response: ${response.body()}")
                    if (response.isSuccessful) {
                        val token = response.body()
                        Log.d("Development", "Successful Login\nToken: ${token?.token}")
                        loginListener.onLoginSuccess()
                        // TODO: Save token to shared preferences
                    } else {
                        Log.d("Development", "LOGIN FAILED, http code : ${response.code()}")
                        // Handle error
                        loginListener.onLoginFailure()
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.d("Development", "LOGIN FAILED, error on delivery : ${t.message}")
                    // Handle error
                    loginListener.onLoginFailure()
                }
            }
        )
    }
}