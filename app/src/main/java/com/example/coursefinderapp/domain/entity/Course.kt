package com.example.coursefinderapp.domain.entity

data class Course(
    val id: Int,
    val title: String,
    val summary: String?,
    val description: String?,
    val rating: Float?,
    val createDate: String?,
    val price: String?,
    val coverImage: String?,
    val sections: List<Int>,
    val authors: List<Int>?
)
