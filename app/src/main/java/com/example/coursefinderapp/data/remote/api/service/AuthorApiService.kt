package com.example.coursefinderapp.data.remote.api.service

import com.example.coursefinderapp.data.remote.api.response.AuthorAndMetaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AuthorApiService {

    @GET("api/users/{id}")
    suspend fun getAuthorById(
        @Path("id") authorId: Int,
        @Header("Authorization") token: String
    ): Response<AuthorAndMetaResponse>
}