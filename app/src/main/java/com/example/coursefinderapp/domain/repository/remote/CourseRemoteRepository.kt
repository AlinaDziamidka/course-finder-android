package com.example.coursefinderapp.domain.repository.remote

import androidx.paging.PagingSource
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.entity.StepikMeta
import com.example.coursefinderapp.domain.util.Event
import kotlinx.coroutines.flow.Flow

interface CourseRemoteRepository {

    fun getCourses(sortOrder: SortOrder, filter: Filters): PagingSource<Int, Course>

    suspend fun fetchCourseById(courseId: Int): Event<Course>
}