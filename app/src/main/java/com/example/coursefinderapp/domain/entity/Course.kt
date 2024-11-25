package com.example.coursefinderapp.domain.entity

import com.google.gson.annotations.SerializedName

data class Course(
    val id: Int,
    val title: String,
    val summary: String?,
    val description: String?,
    val rating: Float?,
    val reviewSummaryId: Int?,
    val createDate: String?,
    val price: String?,
    val tags: List<Int>,
    val isPopular: Boolean,
    val coverImage: String?,
    val sections: List<Int>,
    val authors: List<Int>?
)
