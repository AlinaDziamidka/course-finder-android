package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class AuthorAndMetaResponse(
    @SerializedName("meta")
    val meta: MetaResponse,
    @SerializedName("users")
    val authors: List<AuthorResponse>
)
