package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class MetaResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("has_next")
    val hasNext: Boolean,
    @SerializedName("has_previous")
    val hasPrevious: Boolean
)
