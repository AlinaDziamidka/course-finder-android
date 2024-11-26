package com.example.coursefinderapp.data.remote.repository

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import com.example.coursefinderapp.data.local.cache.CourseCache
import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.paging.CoursesPagingSource
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.transformer.CourseTransformer
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.util.Event
import doCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseRemoteRepositoryImpl @Inject constructor(
    private val courseApiService: CourseApiService,
    private val prefsDataSource: PrefsDataSource
) :
    CourseRemoteRepository {

    override fun getCourses(sortOrder: SortOrder, filter: Filters): PagingSource<Int, Course> {
        return CoursesPagingSource(courseApiService, prefsDataSource, sortOrder, filter)
    }

    override suspend fun fetchCourseById(courseId: Int): Event<Course> {
        val token = withContext(Dispatchers.IO) {
            prefsDataSource.fetchStepikToken()
        } ?: throw Exception("Token is null")

        val event = doCall {
            return@doCall courseApiService.getCourseById(token = token, courseId = courseId)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val transformer = CourseTransformer()
                val metaAndCourses = transformer.fromResponse(response)
                Event.Success(metaAndCourses.second.first())
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}