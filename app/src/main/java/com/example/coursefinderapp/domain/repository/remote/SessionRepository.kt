package com.example.coursefinderapp.domain.repository.remote

import com.example.coursefinderapp.domain.entity.Session
import com.example.coursefinderapp.domain.entity.UserProfile
import com.example.coursefinderapp.domain.util.Event
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun signIn(username: String, password: String): Event<Session>

    suspend fun signUp(username: String, password: String): Event<Session>

    suspend fun fetchToken(): Flow<String?>

    suspend fun saveToken(token: String)

    suspend fun saveUserProfile(userProfile: UserProfile)
}