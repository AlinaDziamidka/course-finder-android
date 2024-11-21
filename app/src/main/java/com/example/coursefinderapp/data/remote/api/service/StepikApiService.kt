package com.example.coursefinderapp.data.remote.api.service


import com.example.coursefinderapp.data.remote.api.response.StepikApiResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface StepikApiService {

    @FormUrlEncoded
    @POST("oauth2/token/")
    suspend fun getToken(
        @Header("Authorization") authorization: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): Response<StepikApiResponse>

}