package com.example.coursefinderapp.di

import com.example.coursefinderapp.data.local.cache.AuthorCache
import com.example.coursefinderapp.data.local.cache.AuthorCacheImpl
import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.BASE_URL_STEPIK
import com.example.coursefinderapp.data.remote.api.service.AuthorApiService
import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.api.service.StepikApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StepikNetworkModule {

    @Provides
    @Named("StepikRetrofit")
    fun provideStepikRetrofit(
        @Named("StepikOkHttp") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STEPIK)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Named("StepikOkHttp")
    fun provideStepikOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideStepikApiService(@Named("StepikRetrofit") retrofit: Retrofit): StepikApiService =
        retrofit.create(StepikApiService::class.java)

    @Provides
    fun provideCourseApiService(@Named("StepikRetrofit") retrofit: Retrofit): CourseApiService =
        retrofit.create(CourseApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthorApiService(@Named("StepikRetrofit") retrofit: Retrofit): AuthorApiService =
        retrofit.create(AuthorApiService::class.java)
}