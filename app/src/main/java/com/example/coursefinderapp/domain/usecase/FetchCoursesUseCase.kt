package com.example.coursefinderapp.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCoursesUseCase @Inject constructor(
    private val courseRemoteRepository: CourseRemoteRepository
) : UseCase<FetchCoursesUseCase.Params, PagingData<Course>> {

    data class Params (
        val sortOrder: SortOrder,
        val filter: Filters
    )

    override suspend fun invoke(params: Params): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                courseRemoteRepository.getCourses(params.sortOrder, params.filter)
            }
        ).flow
    }
}



