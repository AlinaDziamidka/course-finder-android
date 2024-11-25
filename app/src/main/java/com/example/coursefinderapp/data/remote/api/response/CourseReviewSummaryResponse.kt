package com.example.coursefinderapp.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class CourseReviewSummaryResponse(
    @SerializedName("meta")
    val meta: MetaResponse,
    @SerializedName("course-review-summaries")
    val courseReviewSummaries: List<ReviewSummaryResponse>)


data class ReviewSummaryResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("course")
    val course: Int,
    @SerializedName("average")
    val average: Float,
    @SerializedName("count")
    val count: Int,
    @SerializedName("distribution")
    val distribution: List<Int>
)