package com.example.coursefinderapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.transformer.CourseTransformer
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.util.Event
import doCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoursesPagingSource(
    private val courseApiService: CourseApiService,
    private val prefsDataSource: PrefsDataSource
) : PagingSource<Int, Course>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        val currentPage = params.key ?: 1
        val token = withContext(Dispatchers.IO) {
            prefsDataSource.fetchStepikToken()
        } ?: throw Exception("Token is null")

        val event = doCall {
            return@doCall courseApiService.getCourses(
                token = token,
                page = currentPage,
                tag = COURSE_TAG,
                language = COURSE_LANGUAGE
            )
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val transformer = CourseTransformer()
                val metaAndCourses = transformer.fromResponse(response)

                LoadResult.Page(
                    data = metaAndCourses.second,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (metaAndCourses.first.hasNext) currentPage + 1 else null
                )
            }

            is Event.Failure -> {
                LoadResult.Error(Exception(event.exception))
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object{
        const val COURSE_TAG = 1
        const val COURSE_LANGUAGE = "ru"
    }
}