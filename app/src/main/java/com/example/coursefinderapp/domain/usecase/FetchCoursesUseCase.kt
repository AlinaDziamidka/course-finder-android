package com.example.coursefinderapp.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.filter
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.util.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchCoursesUseCase @Inject constructor(
    private val courseRemoteRepository: CourseRemoteRepository,
    private val courseLocalRepository: CourseLocalRepository
) : UseCase<FetchCoursesUseCase.Params, PagingData<Course>> {

    data class Params(
        val sortOrder: SortOrder,
        val filter: Filters
    )

    override suspend fun invoke(params: Params): Flow<PagingData<Course>> {

        val localIds = withContext(Dispatchers.IO) {
            courseLocalRepository.getAllIds()
        }

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                courseRemoteRepository.getCourses(params.sortOrder, params.filter)
            }
        ).flow.map { pagingData ->
            pagingData.filter { course ->
                course.id !in localIds
            }
        }
    }
}



