package com.example.coursefinderapp.domain.repository.local

import com.example.coursefinderapp.domain.entity.Author

interface AuthorLocalRepository {

    suspend fun fetchAll(): List<Author>

    suspend fun fetchById(authorId: Int): Author?

    suspend fun insertOne(author: Author)

    suspend fun deleteById(authorId: Int)

    suspend fun updateOne(author: Author)
}