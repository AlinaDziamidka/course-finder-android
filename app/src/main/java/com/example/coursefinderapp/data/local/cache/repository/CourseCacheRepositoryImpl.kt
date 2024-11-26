package com.example.coursefinderapp.data.local.cache.repository

import com.example.coursefinderapp.data.local.cache.CourseCache
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.cache.CourseCacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CourseCacheRepositoryImpl @Inject constructor(
    private val courseCache: CourseCache
) : CourseCacheRepository {

    override suspend fun fetchCourse(courseId: Int): Flow<Course?> = flow {
        val course = courseCache.fetchCourse(courseId)
        emit(course)
    }

    override suspend fun saveCourse(courseId: Int, course: Course) =
        courseCache.saveCourse(courseId, course)
}