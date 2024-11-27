package com.example.coursefinderapp.domain.usecase

import android.util.Log
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.repository.remote.AuthorRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.util.Event
import com.example.coursefinderapp.domain.util.insertToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncCourseUseCase @Inject constructor(
    private val courseRemoteRepository: CourseRemoteRepository,
    private val courseLocalRepository: CourseLocalRepository,
    private val authorRemoteRepository: AuthorRemoteRepository,
    private val authorLocalRepository: AuthorLocalRepository
) {

    suspend operator fun invoke() {
        syncCourses()
        syncAuthors()
    }

    private suspend fun syncCourses() {
        val localCourses = fetchLocalCourses()
        val courseIds = localCourses.map { it.id }
        val remoteCourses = fetchRemoteCourses(courseIds)

        startCourseSync(localCourses, remoteCourses)
    }

    private suspend fun fetchLocalCourses(): List<Course> = withContext(Dispatchers.IO) {
        courseLocalRepository.fetchAll()
    }

    private suspend fun fetchRemoteCourses(courseIds: List<Int>): MutableList<Course> {
        val remoteCourses = mutableListOf<Course>()

        courseIds.forEach { courseId ->
            val event = courseRemoteRepository.fetchCourseById(courseId)

            when (event) {
                is Event.Success -> remoteCourses.add(event.data)
                is Event.Failure -> Log.e(TAG, "Error fetch courses")
            }
            delay(1000)
        }
        return remoteCourses
    }

    private suspend fun startCourseSync(
        localCourses: List<Course>,
        remoteCourses: MutableList<Course>
    ) {
        remoteCourses.forEach { remoteCourse ->
            val localCourse = localCourses.find { it.id == remoteCourse.id }
            if (localCourse != null) {
                val updatedCourse = remoteCourse.copy(
                    isFavorite = localCourse.isFavorite,
                    isStarted = localCourse.isStarted
                )
                insertToLocalDatabase(courseLocalRepository::updateOne, updatedCourse)
            }
            delay(1000)
        }
    }

    private suspend fun syncAuthors() {
        val localAuthors = fetchLocalAuthors()
        val authorIds = localAuthors.map { it.id }
        val remoteAuthors = fetchRemoteAuthors(authorIds)

        startAuthorSync(localAuthors, remoteAuthors)
    }


    private suspend fun fetchLocalAuthors(): List<Author> = withContext(Dispatchers.IO) {
        authorLocalRepository.fetchAll()
    }

    private suspend fun fetchRemoteAuthors(authorIds: List<Int>): List<Author> {
        val remoteAuthors = mutableListOf<Author>()

        authorIds.forEach { authorId ->
            val event = authorRemoteRepository.fetchAuthorById(authorId)

            when (event) {
                is Event.Success -> remoteAuthors.add(event.data)
                is Event.Failure -> Log.e(TAG, "Error fetch authors")
            }
            delay(1000)
        }
        return remoteAuthors
    }

    private suspend fun startAuthorSync(localAuthors: List<Author>, remoteAuthors: List<Author>) {
        remoteAuthors.forEach { remoteAuthor ->
            val localAuthor = localAuthors.find { it.id == remoteAuthor.id }
            if (localAuthor != null) {
                insertToLocalDatabase(authorLocalRepository::updateOne, remoteAuthor)
            }
            delay(1000)
        }
    }


    companion object {
        private const val TAG = "SyncCourseUseCase"
    }
}