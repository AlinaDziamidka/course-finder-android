package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.util.NonReturningUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteFavoriteCourseUseCase @Inject constructor(
    private val courseLocalRepository: CourseLocalRepository,
    private val authorLocalRepository: AuthorLocalRepository
) : NonReturningUseCase<DeleteFavoriteCourseUseCase.Params> {

    data class Params(
        val course: Course
    )

    override suspend fun invoke(params: Params) {
        val course = params.course
        val authorId = course.authors?.firstOrNull()

        withContext(Dispatchers.IO) {
            courseLocalRepository.deleteById(course.id)
            if (authorId != null) {
                authorLocalRepository.deleteById(authorId)
            }
        }
    }
}