package com.example.coursefinderapp.data.remote.repository

import com.example.coursefinderapp.data.remote.api.service.AuthorApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.transformer.AuthorTransformer
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.repository.remote.AuthorRemoteRepository
import com.example.coursefinderapp.domain.util.Event
import doCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthorRemoteRepositoryImpl @Inject constructor(
    private val authorApiService: AuthorApiService,
    private val prefsDataSource: PrefsDataSource
) : AuthorRemoteRepository {

    override suspend fun fetchAuthorById(authorId: Int): Event<Author> {
        val token = withContext(Dispatchers.IO) {
            prefsDataSource.fetchStepikToken()
        } ?: throw Exception("Token is null")

        val event = doCall {
            return@doCall authorApiService.getAuthorById(token = token, authorId = authorId)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val transformer = AuthorTransformer()
                val author = transformer.fromResponse(response.authors.first())
                Event.Success(author)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }
}