package com.example.coursefinderapp.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("author")
data class AuthorModel (
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("full_name")
    val fullName: String?,
    @ColumnInfo("avatar")
    val avatar: String?
)