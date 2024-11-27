package com.example.coursefinderapp.domain.usecase

import android.util.Log
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.repository.remote.AuthorRemoteRepository
import com.example.coursefinderapp.domain.util.Event
import com.example.coursefinderapp.domain.util.NonReturningUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveFavoriteCourseUseCase @Inject constructor(
    private val courseLocalRepository: CourseLocalRepository,
    private val authorRemoteRepository: AuthorRemoteRepository,
    private val authorLocalRepository: AuthorLocalRepository
) : NonReturningUseCase<SaveFavoriteCourseUseCase.Params> {

    data class Params(
        val course: Course
    )

    override suspend fun invoke(params: Params) {
        val course = params.course
        val authorId = params.course.authors?.first()
        val favoriteCourse = course.copy(isFavorite = true)

        withContext(Dispatchers.IO) {
            courseLocalRepository.insertOne(favoriteCourse)
        }

        try {
            setAuthor(authorId)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving author", e)
        }
    }

    private suspend fun setAuthor(authorId: Int?) {
        Log.d(TAG, "author id: $authorId")
        if (authorId != null) {
            val event = authorRemoteRepository.fetchAuthorById(authorId)

            if (event is Event.Success) {
                Log.d(TAG, "author: ${event.data}")
                withContext(Dispatchers.IO) {
                    authorLocalRepository.insertOne(event.data)
                }
            }
        }
    }

    companion object {
        private const val TAG = "SaveFavoriteCourseUseCase"
    }
}

