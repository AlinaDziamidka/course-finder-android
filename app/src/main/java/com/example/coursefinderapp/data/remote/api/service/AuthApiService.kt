package com.example.coursefinderapp.data.remote.api.service

import com.example.coursefinderapp.data.remote.api.request.SignInRequest
import com.example.coursefinderapp.data.remote.api.request.SignUpRequest
import com.example.coursefinderapp.data.remote.api.response.SignInResponse
import com.example.coursefinderapp.data.remote.api.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {

    @POST("./accounts:signInWithPassword")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>


    @POST("./accounts:signUp")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>
}