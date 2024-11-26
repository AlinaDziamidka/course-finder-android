package com.example.coursefinderapp.domain.repository.local.cache

import com.example.coursefinderapp.domain.entity.Course
import kotlinx.coroutines.flow.Flow

interface CourseCacheRepository {

    suspend fun fetchCourse(courseId: Int): Flow<Course?>

    suspend fun saveCourse(courseId: Int, course: Course)
}