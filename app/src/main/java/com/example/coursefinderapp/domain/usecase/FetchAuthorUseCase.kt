package com.example.coursefinderapp.domain.usecase

import android.util.Log
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import com.example.coursefinderapp.domain.repository.local.cache.AuthorCacheRepository
import com.example.coursefinderapp.domain.repository.remote.AuthorRemoteRepository
import com.example.coursefinderapp.domain.util.DataSource
import com.example.coursefinderapp.domain.util.Event
import com.example.coursefinderapp.domain.util.UseCase
import com.example.coursefinderapp.domain.util.insertToCache
import com.example.coursefinderapp.domain.util.insertToLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchAuthorUseCase @Inject constructor(
    private val authorRemoteRepository: AuthorRemoteRepository,
    private val authorCacheRepository: AuthorCacheRepository,
    private val authorLocalRepository: AuthorLocalRepository
) : UseCase<FetchAuthorUseCase.Params, Author> {

    data class Params(
        val course: Course,
        val dataSource: String
    )

    override suspend fun invoke(params: Params): Flow<Author> = flow {
        val courseId = params.course.id
        val authorId = params.course.authors?.first() ?: throw Exception("Field authors is null")
        val dataSource = try {
            DataSource.valueOf(params.dataSource)
        } catch (e: IllegalArgumentException) {
            null
        }

        Log.d("FetchAuthorUseCase", "$dataSource")

        val author = when (dataSource) {
            DataSource.CACHE -> loadFromCache(courseId, authorId)
            DataSource.DATABASE -> loadFromDatabase(authorId)
            else -> loadFromRemoteStorage(authorId)
        }
        emit(author)
    }

    private suspend fun loadFromCache(courseId: Int, authorId: Int): Author {
        val authorFromCache = authorCacheRepository.fetchAuthorByCourseId(courseId).firstOrNull()

        return if (authorFromCache == null) {
            val authorFromRemoteStorage = loadFromRemoteStorage(authorId)
            insertToCache(
                authorCacheRepository::saveCourseAuthor,
                courseId,
                authorFromRemoteStorage
            )
            authorFromRemoteStorage
        } else {
            authorFromCache
        }
    }

    private suspend fun loadFromDatabase(authorId: Int): Author {
        val authorFromDataBase = withContext(Dispatchers.IO) {
            authorLocalRepository.fetchById(authorId)
        }

        return if (authorFromDataBase == null) {
            val authorFromRemoteStorage = loadFromRemoteStorage(authorId)
            insertToLocalDatabase(authorLocalRepository::insertOne, authorFromRemoteStorage)
            authorFromRemoteStorage
        } else {
            authorFromDataBase
        }
    }


    private suspend fun loadFromRemoteStorage(authorId: Int): Author {
        authorRemoteRepository.fetchAuthorById(authorId).let { event ->
            return when (event) {
                is Event.Success -> event.data
                is Event.Failure -> throw Exception(event.exception)
            }
        }
    }
}