package com.example.coursefinderapp.data.remote.transformer

import com.example.coursefinderapp.data.remote.api.response.CourseAndMetaResponse
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.StepikMeta

class CourseTransformer {

    fun fromResponse(response: CourseAndMetaResponse): Pair<StepikMeta, List<Course>> {
        val stepikMeta = StepikMeta(
            page = response.meta.page,
            hasNext = response.meta.hasNext,
            hasPrevious = response.meta.hasPrevious
        )

        val courses = response.courses.map { course ->
            Course(
                id = course.id,
                title = course.title,
                summary = course.summary,
                description = course.description,
                rating = null,
                reviewSummaryId = course.reviewSummary,
                createDate = course.createDate,
                price = course.price,
                tags = course.tags,
                isPopular = course.isPopular,
                courseUrl = course.courseUrl,
                coverImage = course.coverImage,
                authors = course.authors
            )
        }
        return stepikMeta to courses
    }
}