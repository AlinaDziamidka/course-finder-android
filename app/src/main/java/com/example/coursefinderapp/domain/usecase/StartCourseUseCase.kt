package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.util.NonReturningUseCase
import com.example.coursefinderapp.domain.util.insertToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartCourseUseCase @Inject constructor(
    private val courseLocalRepository: CourseLocalRepository
) : NonReturningUseCase<StartCourseUseCase.Params> {

    data class Params(
        val course: Course
    )

    override suspend fun invoke(params: Params) {
        val course = params.course
        val isFavorite = checkIsCourseFavorite(course)

        if (isFavorite){
            insertToLocalDatabase(courseLocalRepository::insertOne, course.copy(isFavorite = true, isStarted = true))
        } else {
            insertToLocalDatabase(courseLocalRepository::insertOne, course.copy(isStarted = true))
        }
    }

    private suspend fun checkIsCourseFavorite(course: Course): Boolean =
        withContext(Dispatchers.IO) {
            courseLocalRepository.fetchById(course.id) != null
        }
}