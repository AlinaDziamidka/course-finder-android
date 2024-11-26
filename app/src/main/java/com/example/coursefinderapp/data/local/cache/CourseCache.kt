package com.example.coursefinderapp.data.local.cache

import com.example.coursefinderapp.domain.entity.Course

class CourseCacheImpl : CourseCache {

    private val memoryCache = mutableMapOf<Int, Course>()

    override fun saveCourse(courseId: Int, course: Course) {
        memoryCache[courseId] = course
    }

    override fun fetchCourse(courseId: Int): Course? = memoryCache[courseId]

    override fun clearCache() {
        memoryCache.clear()
    }
}

interface CourseCache {
    fun saveCourse(courseId: Int, course: Course)
    fun fetchCourse(courseId: Int): Course?
    fun clearCache()
}
