package com.example.coursefinderapp.data.remote.transformer

import android.util.Log
import com.example.coursefinderapp.data.remote.api.response.StepikCourseResponse
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.StepikMeta

class CourseTransformer {

    fun fromResponse(response: StepikCourseResponse): Pair<StepikMeta, List<Course>> {
        Log.d("CourseTransformer", "Number of courses in response: ${response.courses.size}")
        val stepikMeta = StepikMeta(
            page = response.meta.page,
            hasNext = response.meta.hasNext,
            hasPrevious = response.meta.hasPrevious
        )

        val courses = response.courses.map { course ->
            Log.d("CourseTransformer", "Processing course: ${course.id}, Title: ${course.title}")
            Course(
                id = course.id,
                title = course.title,
                summary = course.summary,
                description = course.description,
                rating = course.rating,
                createDate = course.createDate,
                price = course.price,
                coverImage = course.coverImage,
                sections = course.sections,
                authors = course.authors
            )
        }
        return stepikMeta to courses
    }
}