package com.example.coursefinderapp.domain.repository.remote

import androidx.paging.PagingSource
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.StepikMeta
import com.example.coursefinderapp.domain.util.Event

interface CourseRemoteRepository {

    fun getCourses(): PagingSource<Int, Course>
}