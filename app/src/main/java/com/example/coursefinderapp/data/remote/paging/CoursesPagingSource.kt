package com.example.coursefinderapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.transformer.CourseTransformer
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.util.Event
import doCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CoursesPagingSource(
    private val courseApiService: CourseApiService,
    private val prefsDataSource: PrefsDataSource,
    private val sortOrder: SortOrder,
    private val filter: Filters
) : PagingSource<Int, Course>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        val currentPage = params.key ?: 1
        val token = withContext(Dispatchers.IO) {
            prefsDataSource.fetchStepikToken()
        } ?: throw Exception("Token is null")

        return try {
            val event = doCall {
                return@doCall courseApiService.getCourses(
                    token = token,
                    page = currentPage,
                    tag = COURSE_TAG,
                    language = COURSE_LANGUAGE
                )
            }

            when (event) {
                is Event.Success -> {
                    val response = event.data
                    val transformer = CourseTransformer()
                    val metaAndCourses = transformer.fromResponse(response)

                    val coursesWithRatings = coroutineScope {
                        metaAndCourses.second.map { course ->
                            async {
                                if (course.reviewSummaryId != null) {
                                    val reviewSummary =
                                        getCourseReviewSummary(course.reviewSummaryId, token)
                                    course.copy(rating = reviewSummary)
                                } else {
                                    course.copy(rating = null)
                                }
                            }
                        }.awaitAll()
                    }

                    val sortedCourses = sortCourses(coursesWithRatings, sortOrder)
                    val sortedAndFilteredCourses = filterCourses(sortedCourses, filter)

                    LoadResult.Page(
                        data = sortedAndFilteredCourses,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (metaAndCourses.first.hasNext) currentPage + 1 else null
                    )
                }

                is Event.Failure -> {
                    LoadResult.Error(Exception(event.exception))
                }
            }
        } catch (e: SocketTimeoutException) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getCourseReviewSummary(
        reviewSummaryId: Int,
        token: String
    ): Float? {
        val event = doCall { courseApiService.getCourseReviewSummary(reviewSummaryId, token) }
        return when (event) {
            is Event.Success -> {
                event.data.courseReviewSummaries.first().average
            }

            is Event.Failure -> {
                null
            }
        }
    }

    private fun sortCourses(courses: List<Course>, sortOrder: SortOrder): List<Course> {
        return when (sortOrder) {
            SortOrder.BY_DATE -> courses.sortedByDescending { it.createDate }
            SortOrder.BY_POPULARITY -> courses.sortedByDescending { it.isPopular }
            SortOrder.BY_RATING -> courses.sortedByDescending { it.rating }
        }
    }

    private fun filterCourses(courses: List<Course>, filter: Filters): List<Course> {
        return when (filter) {

            is Filters.Category -> {

                when (filter.type) {
                    Filters.Category.Type.PYTHON -> {
                        courses.filter { course -> course.tags.contains(Filters.Category.Type.PYTHON.code) }
                    }

                    Filters.Category.Type.C_SHARP -> {
                        courses.filter { course -> course.tags.contains(Filters.Category.Type.C_SHARP.code) }
                    }

                    Filters.Category.Type.C -> {
                        courses.filter { course -> course.tags.contains(Filters.Category.Type.C.code) }
                    }

                    Filters.Category.Type.JAVA -> {
                        courses.filter { course -> course.tags.contains(Filters.Category.Type.JAVA.code) }
                    }

                    Filters.Category.Type.KOTLIN -> {
                        courses.filter { course -> course.tags.contains(Filters.Category.Type.KOTLIN.code) }
                    }
                }
            }

            is Filters.Price -> {

                when (filter.type) {
                    Filters.Price.Type.FREE -> {
                        courses.filter { course -> course.price == null }
                    }

                    Filters.Price.Type.PAID -> {
                        courses.filter { course -> course.price != null }
                    }
                }
            }

            Filters.NoFilter -> {
                courses
            }
        }
    }

    override fun getRefreshKey(
        state: PagingState<Int, Course>
    ): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    companion object {
        const val COURSE_TAG = 1
        const val COURSE_LANGUAGE = "ru"
    }
}

