package com.example.coursefinderapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.coursefinderapp.data.local.database.model.CourseModel

@Dao
interface CourseDao {

    @Query("SELECT * FROM course")
    fun fetchAll(): List<CourseModel>

    @Query("SELECT id FROM course")
    fun getAllIds(): List<Int>

    @Query("SELECT * FROM course WHERE id = :courseId LIMIT 1")
    fun fetchById(courseId: Int): CourseModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(courseModel: CourseModel)

    @Query("DELETE FROM course WHERE id = :courseId")
    fun deleteById(courseId: Int)

    @Update
    fun updateOne(courseModel: CourseModel)
}