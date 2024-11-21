package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class SignInResponse (
    @SerializedName("idToken")
    val idToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresIn")
    val expiresIn: String,
    @SerializedName("localId")
    val localId: String
)
