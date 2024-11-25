package com.example.coursefinderapp.data.remote.repository

import android.util.Log
import androidx.paging.PagingSource
import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.paging.CoursesPagingSource
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import javax.inject.Inject

class CourseRemoteRepositoryImpl @Inject constructor(
    private val courseApiService: CourseApiService,
    private val prefsDataSource: PrefsDataSource
) :
    CourseRemoteRepository {

    override fun getCourses(sortOrder: SortOrder, filter: Filters): PagingSource<Int, Course> {
        Log.d("CourseRemoteRepositoryImpl", "startInvoke: $sortOrder, $filter")
        return CoursesPagingSource(courseApiService, prefsDataSource, sortOrder, filter)
    }
}