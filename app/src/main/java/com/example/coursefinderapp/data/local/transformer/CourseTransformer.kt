package com.example.coursefinderapp.data.local.transformer

import com.example.coursefinderapp.data.local.database.model.CourseModel
import com.example.coursefinderapp.domain.entity.Course

class CourseTransformer {

    fun fromModel(model: CourseModel): Course {
        return Course(
            id = model.id,
            title = model.title,
            summary = model.summary,
            description = model.description,
            createDate = model.createDate,
            price = model.price,
            rating = model.rating,
            tags = model.tags,
            reviewSummaryId = model.reviewSummary,
            isPopular = model.isPopular,
            courseUrl = model.courseUrl,
            coverImage = model.image,
            authors = model.authors
        )
    }

    fun toModel(course: Course): CourseModel {
        return CourseModel(
            id = course.id,
            title = course.title,
            summary = course.summary,
            description = course.description,
            createDate = course.createDate,
            price = course.price,
            rating = course.rating,
            tags = course.tags,
            reviewSummary = course.reviewSummaryId,
            isPopular = course.isPopular,
            courseUrl = course.courseUrl,
            image = course.coverImage,
            authors = course.authors
        )
    }
}