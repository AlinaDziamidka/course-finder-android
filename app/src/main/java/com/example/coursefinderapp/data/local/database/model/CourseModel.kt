package com.example.coursefinderapp.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("course")
data class CourseModel(
    @PrimaryKey()
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("summary")
    val summary: String?,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("create_date")
    val createDate: String?,
    @ColumnInfo("price")
    val price: String?,
    @ColumnInfo("rating")
    val rating: Float?,
    @ColumnInfo("tags")
    val tags: List<Int>,
    @ColumnInfo("review_summary")
    val reviewSummary: Int?,
    @ColumnInfo("is_popular")
    val isPopular: Boolean,
    @ColumnInfo("course_url")
    val courseUrl: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("authors")
    val authors: List<Int>?
)