package com.example.coursefinderapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.coursefinderapp.data.local.database.dao.AuthorDao
import com.example.coursefinderapp.data.local.database.dao.CourseDao
import com.example.coursefinderapp.data.local.database.model.AuthorModel
import com.example.coursefinderapp.data.local.database.model.CourseModel


@Database(
    entities = [CourseModel::class, AuthorModel::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class Database : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun authorDao(): AuthorDao
}