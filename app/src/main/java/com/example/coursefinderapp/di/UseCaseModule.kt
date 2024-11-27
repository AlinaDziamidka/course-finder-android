package com.example.coursefinderapp.di

import com.example.coursefinderapp.domain.repository.local.AuthorLocalRepository
import com.example.coursefinderapp.domain.repository.local.CourseLocalRepository
import com.example.coursefinderapp.domain.repository.local.cache.AuthorCacheRepository
import com.example.coursefinderapp.domain.repository.local.cache.CourseCacheRepository
import com.example.coursefinderapp.domain.repository.remote.AuthorRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.CourseRemoteRepository
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.usecase.FetchAuthorUseCase
import com.example.coursefinderapp.domain.usecase.FetchCourseUseCase
import com.example.coursefinderapp.domain.usecase.FetchCoursesUseCase
import com.example.coursefinderapp.domain.usecase.FetchFavoriteCoursesUseCase
import com.example.coursefinderapp.domain.usecase.SaveCourseToCacheUseCase
import com.example.coursefinderapp.domain.usecase.SaveFavoriteCourseUseCase

import com.example.coursefinderapp.domain.usecase.SignInUseCase
import com.example.coursefinderapp.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

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
    fun provideFetchCoursesUseCase(
        courseRemoteRepository: CourseRemoteRepository,
        courseLocalRepository: CourseLocalRepository
    ): FetchCoursesUseCase {
        return FetchCoursesUseCase(courseRemoteRepository, courseLocalRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSaveCourseToCacheUseCase(repository: CourseCacheRepository): SaveCourseToCacheUseCase {
        return SaveCourseToCacheUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideFetchCourseFromCacheUseCase(
        courseRemoteRepository: CourseRemoteRepository,
        courseCacheRepository: CourseCacheRepository,
        courseLocalRepository: CourseLocalRepository
    ): FetchCourseUseCase {
        return FetchCourseUseCase(
            courseRemoteRepository,
            courseCacheRepository,
            courseLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideFetchAuthorUseCase(
        authorRemoteRepository: AuthorRemoteRepository,
        authorCacheRepository: AuthorCacheRepository,
        authorLocalRepository: AuthorLocalRepository
    ): FetchAuthorUseCase {
        return FetchAuthorUseCase(
            authorRemoteRepository,
            authorCacheRepository,
            authorLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideSaveFavoriteCourseUseCase(
        courseLocalRepository: CourseLocalRepository,
        authorRemoteRepository: AuthorRemoteRepository,
        authorLocalRepository: AuthorLocalRepository
    ): SaveFavoriteCourseUseCase {
        return SaveFavoriteCourseUseCase(
            courseLocalRepository,
            authorRemoteRepository,
            authorLocalRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideFetchFavoriteCoursesUseCase(repository: CourseLocalRepository): FetchFavoriteCoursesUseCase {
        return FetchFavoriteCoursesUseCase(repository)
    }


}