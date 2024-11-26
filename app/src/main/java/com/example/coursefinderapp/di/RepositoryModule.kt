package com.example.coursefinderapp.di

import com.example.coursefinderapp.data.local.cache.AuthorCache
import com.example.coursefinderapp.data.local.cache.CourseCache
import com.example.coursefinderapp.data.local.cache.repository.AuthorCacheRepositoryImpl
import com.example.coursefinderapp.data.local.cache.repository.CourseCacheRepositoryImpl
import com.example.coursefinderapp.data.local.database.dao.AuthorDao
import com.example.coursefinderapp.data.local.database.dao.CourseDao
import com.example.coursefinderapp.data.local.repository.AuthorLocalRepositoryImpl
import com.example.coursefinderapp.data.local.repository.CourseLocalRepositoryImpl
import com.example.coursefinderapp.data.remote.api.service.AuthorApiService
import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.api.service.StepikApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.repository.AuthorRemoteRepositoryImpl
import com.example.coursefinderapp.data.remote.repository.CourseRemoteRepositoryImpl
import com.example.coursefinderapp.data.remote.repository.SessionRepositoryImpl
import com.example.coursefinderapp.data.remote.repository.StepikAuthRepositoryImpl
import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.repository.local.cache.AuthorCacheRepository
import com.example.coursefinderapp.domain.repository.local.cache.CourseCacheRepository
import com.example.coursefinderapp.domain.repository.remote.AuthorRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.repository.remote.StepikAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    @Singleton
    fun provideStepikAuthRepository(
        prefsDataSource: PrefsDataSource,
        stepikApiService: StepikApiService
    ): StepikAuthRepository {
        return StepikAuthRepositoryImpl(prefsDataSource, stepikApiService)
    }

    @Provides
    @Singleton
    fun provideCourseRemoteRepository(
        courseApiService: CourseApiService,
        prefsDataSource: PrefsDataSource
    ): CourseRemoteRepository {
        return CourseRemoteRepositoryImpl(courseApiService, prefsDataSource)
    }

    @Provides
    @Singleton
    fun provideCourseCacheRepository(
        courseCache: CourseCache
    ): CourseCacheRepository {
        return CourseCacheRepositoryImpl(courseCache)
    }

    @Provides
    @Singleton
    fun provideAuthorRemoteRepository(
        authorApiService: AuthorApiService,
        prefsDataSource: PrefsDataSource
    ): AuthorRemoteRepository {
        return AuthorRemoteRepositoryImpl(authorApiService, prefsDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthorCacheRepository(
        authorCache: AuthorCache
    ): AuthorCacheRepository {
        return AuthorCacheRepositoryImpl(authorCache)
    }

    @Provides
    @Singleton
    fun provideCourseLocalRepository(
        courseDao: CourseDao
    ): CourseLocalRepository {
        return CourseLocalRepositoryImpl(courseDao)
    }

    @Provides
    @Singleton
    fun provideAuthorLocalRepository(
        authorDao: AuthorDao
    ): AuthorLocalRepository {
        return AuthorLocalRepositoryImpl(authorDao)
    }

}


@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository
}