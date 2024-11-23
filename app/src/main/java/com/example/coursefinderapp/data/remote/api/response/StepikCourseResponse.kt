package com.example.coursefinderapp.data.remote.api.response

import com.example.coursefinderapp.domain.entity.StepikMeta
import com.google.gson.annotations.SerializedName

data class StepikCourseResponse(
    @SerializedName("meta")
    val meta: MetaResponse,
    @SerializedName("courses")
    val courses: List<CourseResponse>
)

data class CourseResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("create_date")
    val createDate: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("cover_image")
    val coverImage: String?,
    @SerializedName("sections")
    val sections: List<Int>,
    @SerializedName("authors")
    val authors: List<Int>?
)

data class MetaResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("has_next")
    val hasNext: Boolean,
    @SerializedName("has_previous")
    val hasPrevious: Boolean
)