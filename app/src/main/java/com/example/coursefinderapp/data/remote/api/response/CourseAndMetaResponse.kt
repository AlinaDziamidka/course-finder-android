package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class CourseAndMetaResponse(
    @SerializedName("meta")
    val meta: MetaResponse,
    @SerializedName("courses")
    val courses: List<CourseResponse>
)