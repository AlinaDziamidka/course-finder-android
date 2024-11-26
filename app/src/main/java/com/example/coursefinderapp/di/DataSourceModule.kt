package com.example.coursefinderapp.di

import android.content.Context
import com.example.coursefinderapp.data.local.cache.AuthorCache
import com.example.coursefinderapp.data.local.cache.AuthorCacheImpl
import com.example.coursefinderapp.data.local.cache.CourseCache
import com.example.coursefinderapp.data.local.cache.CourseCacheImpl
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun providePrefsDataSource(@ApplicationContext appContext: Context): PrefsDataSource {
        return PrefsDataSourceImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideCourseCache(): CourseCache = CourseCacheImpl()

    @Provides
    @Singleton
    fun provideAuthorCache(): AuthorCache = AuthorCacheImpl()
}