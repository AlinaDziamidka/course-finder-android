package com.example.coursefinderapp.domain.repository.remote

import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.util.Event

interface AuthorRemoteRepository {

    suspend fun fetchAuthorById(authorId: Int): Event<Author>
}