package com.example.coursefinderapp.di

import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.usecase.FetchCoursesUseCase

import com.example.coursefinderapp.domain.usecase.SignInUseCase
import com.example.coursefinderapp.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(repository: SessionRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(repository: SessionRepository): SignUpUseCase {
        return SignUpUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchCoursesUseCase(repository: CourseRemoteRepository): FetchCoursesUseCase {
        return FetchCoursesUseCase(repository)
    }
}