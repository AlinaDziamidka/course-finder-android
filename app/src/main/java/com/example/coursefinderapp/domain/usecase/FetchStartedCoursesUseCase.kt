package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchStartedCoursesUseCase @Inject constructor(
    private val courseLocalRepository: CourseLocalRepository
) : UseCase<Unit, List<Course>> {

    override suspend fun invoke(params: Unit): Flow<List<Course>> = flow {
        val courses = withContext(Dispatchers.IO) {
            courseLocalRepository.fetchAll()
        }
        val startedCourses = courses.filter { course -> course.isStarted }

       emit(startedCourses)
    }
}