package com.example.coursefinderapp.domain.repository.local.cache

import com.example.coursefinderapp.domain.entity.Author
import kotlinx.coroutines.flow.Flow

interface AuthorCacheRepository {

    suspend fun saveCourseAuthor(courseId: Int, author: Author)
    suspend fun fetchAuthorByCourseId(courseId: Int): Flow<Author?>
    suspend fun saveAuthor(authorId: Int, author: Author)
    suspend fun fetchAuthor(authorId: Int): Flow<Author?>
}