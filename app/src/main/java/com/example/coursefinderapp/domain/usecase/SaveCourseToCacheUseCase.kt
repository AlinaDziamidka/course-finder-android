package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.cache.CourseCacheRepository
import com.example.coursefinderapp.domain.util.NonReturningUseCase
import javax.inject.Inject

class SaveCourseToCacheUseCase @Inject constructor(
    private val courseCacheRepository: CourseCacheRepository
) : NonReturningUseCase<SaveCourseToCacheUseCase.Params> {

    data class Params(
        val course: Course
    )

    override suspend fun invoke(params: Params) {
        val course = params.course
        return courseCacheRepository.saveCourse(course.id, course)
    }
}