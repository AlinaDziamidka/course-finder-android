package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.repository.local.cache.CourseCacheRepository
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.util.DataSource
import com.example.coursefinderapp.domain.util.Event
import com.example.coursefinderapp.domain.util.UseCase
import com.example.coursefinderapp.domain.util.insertToCache
import com.example.coursefinderapp.domain.util.insertToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchCourseUseCase @Inject constructor(
    private val courseRemoteRepository: CourseRemoteRepository,
    private val courseCacheRepository: CourseCacheRepository,
    private val courseLocalRepository: CourseLocalRepository
) : UseCase<FetchCourseUseCase.Params, Course> {

    data class Params(
        val courseId: Int,
        val dataSource: String
    )

    override suspend fun invoke(params: Params): Flow<Course> = flow {
        val courseId = params.courseId
        val dataSource = try {
            DataSource.valueOf(params.dataSource)
        } catch (e: IllegalArgumentException) {
            null
        }

        val course = when (dataSource) {
            DataSource.CACHE -> loadFromCache(courseId)
            DataSource.DATABASE -> loadFromDatabase(courseId)
            else -> loadFromRemoteStorage(courseId)
        }
        emit(course)
    }

    private suspend fun loadFromCache(courseId: Int): Course {
        val courseFromCache = courseCacheRepository.fetchCourse(courseId).firstOrNull()
        val courseFromRemoteStorage = loadFromRemoteStorage(courseId)

        insertToCache(courseCacheRepository::saveCourse, courseId, courseFromRemoteStorage)

        return courseFromCache ?: courseFromRemoteStorage
    }

    private suspend fun loadFromDatabase(courseId: Int): Course {
        val courseFromDataBase = withContext(Dispatchers.IO) {
            courseLocalRepository.fetchById(courseId)
        }
        val courseFromRemoteStorage = loadFromRemoteStorage(courseId)

        insertToLocalDatabase(courseLocalRepository::insertOne,courseFromRemoteStorage)

        return courseFromDataBase ?: courseFromRemoteStorage
    }


    private suspend fun loadFromRemoteStorage(courseId: Int): Course {
        courseRemoteRepository.fetchCourseById(courseId).let { event ->
            return when (event) {
                is Event.Success -> event.data
                is Event.Failure -> throw Exception(event.exception)
            }
        }
    }
}

