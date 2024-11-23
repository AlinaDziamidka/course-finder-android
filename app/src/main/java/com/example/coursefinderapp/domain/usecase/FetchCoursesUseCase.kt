package com.example.coursefinderapp.domain.usecase


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCoursesUseCase @Inject constructor(
    private val courseRemoteRepository: CourseRemoteRepository
) : UseCase<Unit, PagingData<Course>> {

    override suspend fun invoke(params: Unit): Flow<PagingData<Course>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { courseRemoteRepository.getCourses() }
        ).flow
    }
}



