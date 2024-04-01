package com.example.myapplication.model.auth

import com.google.gson.annotations.SerializedName

data class UserCred (
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
    )