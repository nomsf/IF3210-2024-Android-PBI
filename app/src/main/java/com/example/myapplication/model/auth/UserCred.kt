package com.example.myapplication.model.auth

import com.google.gson.annotations.SerializedName

data class UserCred (
    @SerializedName("nim")
    val nim: String,
    @SerializedName("iat")
    val iat: String,
    @SerializedName("exp")
    val exp: String
    )