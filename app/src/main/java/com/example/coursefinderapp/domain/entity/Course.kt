package com.example.coursefinderapp.domain.entity


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
    val isStarted: Boolean,
    val lessonCount: Int?,
    val isFavorite: Boolean,
    val courseUrl: String?,
    val coverImage: String?,
    val authors: List<Int>?
)
