package com.example.coursefinderapp.di

import com.example.coursefinderapp.data.remote.api.service.CourseApiService
import com.example.coursefinderapp.data.remote.api.service.StepikApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.repository.CourseRemoteRepositoryImpl
import com.example.coursefinderapp.data.remote.repository.SessionRepositoryImpl
import com.example.coursefinderapp.data.remote.repository.StepikAuthRepositoryImpl
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.repository.remote.StepikAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
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
}

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryBindModule {

    @Binds
    fun bindSessionRepository(sessionRepositoryImpl: SessionRepositoryImpl): SessionRepository
}