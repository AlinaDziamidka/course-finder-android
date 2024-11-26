package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("avatar")
    val avatar: String?
)
