package com.example.coursefinderapp.domain.repository.local

import com.example.coursefinderapp.domain.entity.Course

interface CourseLocalRepository {

    suspend fun fetchAll(): List<Course>

    suspend fun fetchById(courseId: Int): Course?

    suspend fun insertOne(course: Course)

    suspend fun deleteById(courseId: Int)

    suspend fun updateOne(course: Course)
}
