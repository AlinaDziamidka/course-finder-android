package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class StepikApiResponse(




    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int
)
