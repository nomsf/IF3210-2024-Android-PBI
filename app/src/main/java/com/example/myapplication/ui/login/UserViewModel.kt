package com.example.myapplication.ui.login


import androidx.lifecycle.ViewModel


class UserViewModel() : ViewModel(){
    var email: String = ""
    var password: String = ""
    var loginSuccesful: Boolean = false


    fun setUser(email: String, password: String){
        this.email = email
        this.password = password
    }


}