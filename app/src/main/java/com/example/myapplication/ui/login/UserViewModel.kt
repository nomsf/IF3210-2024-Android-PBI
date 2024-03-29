package com.example.myapplication.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.util.LoginListener

class UserViewModel() : ViewModel(){
    var email: String = ""
    var password: String = ""
    var loginSuccesful: Boolean = false


    fun setUser(email: String, password: String){
        this.email = email
        this.password = password
    }


}