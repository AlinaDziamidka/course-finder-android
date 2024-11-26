package com.example.coursefinderapp.di

import android.content.Context
import androidx.room.Room
import com.example.coursefinderapp.data.local.database.Database
import com.example.coursefinderapp.data.local.database.dao.AuthorDao
import com.example.coursefinderapp.data.local.database.dao.CourseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: Database): CourseDao {
        return database.courseDao()
    }

    @Provides
    fun provideAuthorDao(database: Database): AuthorDao {
        return database.authorDao()
    }

}