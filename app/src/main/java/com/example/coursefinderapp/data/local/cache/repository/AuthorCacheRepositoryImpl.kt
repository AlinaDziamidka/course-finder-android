package com.example.coursefinderapp.data.local.cache.repository

import com.example.coursefinderapp.data.local.cache.AuthorCache
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.repository.local.cache.AuthorCacheRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthorCacheRepositoryImpl @Inject constructor(
    private val authorCache: AuthorCache
) : AuthorCacheRepository {

    override suspend fun saveCourseAuthor(courseId: Int, author: Author) {
        authorCache.saveCourseAuthor(courseId, author)
    }

    override suspend fun fetchAuthorByCourseId(courseId: Int): Flow<Author?> = flow {
        val author = authorCache.fetchAuthorByCourseId(courseId)
        emit(author)
    }

    override suspend fun saveAuthor(authorId: Int, author: Author) {
        authorCache.saveAuthor(author.id, author)
    }

    override suspend fun fetchAuthor(authorId: Int): Flow<Author?> = flow {
        val author = authorCache.fetchAuthor(authorId)
        emit(author)
    }
}