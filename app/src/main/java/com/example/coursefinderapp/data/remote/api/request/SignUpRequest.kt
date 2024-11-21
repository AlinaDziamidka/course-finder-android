package com.example.coursefinderapp.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("returnSecureToken")
    val returnSecureToken: Boolean = true
)
