package com.example.coursefinderapp.data.remote.repository

import com.example.coursefinderapp.data.remote.api.request.SignInRequest
import com.example.coursefinderapp.data.remote.api.request.SignUpRequest
import com.example.coursefinderapp.data.remote.api.service.AuthApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.domain.entity.Session
import com.example.coursefinderapp.domain.entity.UserProfile
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.util.Event
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val prefsDataSource: PrefsDataSource,
    private val authApiService: AuthApiService
) : SessionRepository {

    override suspend fun signIn(username: String, password: String): Event<Session> {
        val event = doCall {
            val request = SignInRequest(username, password)
            return@doCall authApiService.signIn(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val session = Session(
                    token = response.idToken,
                    userProfile = UserProfile(
                        userId = response.localId,
                        username = response.email,
                    )
                )
                Event.Success(session)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun signUp(username: String, password: String): Event<Session> {
        val event = doCall {
            val request = SignUpRequest(email = username, password = password)
            return@doCall authApiService.signUp(request)
        }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val session = Session(
                    token = "",
                    userProfile = UserProfile(
                        userId = response.localId,
                        username = response.email ?: "")
                )
                Event.Success(session)
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }

    override suspend fun fetchToken(): Flow<String?> = flow {
        val token = prefsDataSource.fetchToken()
        emit(token)
    }

    override suspend fun saveToken(token: String) = prefsDataSource.saveToken(token)

    override suspend fun saveUserProfile(userProfile: UserProfile) =
        prefsDataSource.saveUserProfile(userProfile)
}