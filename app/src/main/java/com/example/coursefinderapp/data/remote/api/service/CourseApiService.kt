package com.example.coursefinderapp.data.remote.api.service

import com.example.coursefinderapp.data.remote.api.response.StepikCourseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CourseApiService {

    @GET("api/courses")
    suspend fun getCourses(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("tag") tag: Int? = null,
        @Query("language") language: String? = null
    ): Response<StepikCourseResponse>



}