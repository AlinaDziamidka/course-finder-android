package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class CourseResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("create_date")
    val createDate: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("tags")
    val tags: List<Int>,
    @SerializedName("is_popular")
    val isPopular: Boolean,
    @SerializedName("canonical_url")
    val courseUrl: String?,
    @SerializedName("lessons_count")
    val lessonCount: Int?,
    @SerializedName("review_summary")
    val reviewSummary: Int?,
    @SerializedName("cover")
    val coverImage: String?,
    @SerializedName("authors")
    val authors: List<Int>?
)
