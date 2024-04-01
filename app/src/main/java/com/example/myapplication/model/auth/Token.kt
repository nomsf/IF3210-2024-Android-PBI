package com.example.myapplication.model.auth

import com.google.gson.annotations.SerializedName

data class Token (
    @SerializedName("token")
    val token: String
)