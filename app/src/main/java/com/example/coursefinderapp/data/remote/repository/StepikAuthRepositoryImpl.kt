package com.example.coursefinderapp.data.remote.repository

import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.CLIENT_ID_STEPIK
import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.CLIENT_SECRET_STEPIK
import com.example.coursefinderapp.data.remote.api.service.StepikApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.domain.repository.remote.StepikAuthRepository
import com.example.coursefinderapp.domain.util.Event
import doCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Credentials
import javax.inject.Inject

class StepikAuthRepositoryImpl @Inject constructor(
    private val prefsDataSource: PrefsDataSource,
    private val stepikApiService: StepikApiService
) : StepikAuthRepository {

    override suspend fun getStepikToken(): Event<String> {
        val credentials = Credentials.basic(CLIENT_ID_STEPIK, CLIENT_SECRET_STEPIK)
        val event = doCall { return@doCall stepikApiService.getToken(credentials) }

        return when (event) {
            is Event.Success -> {
                val response = event.data
                val token = response.accessToken
                if (!token.isNullOrEmpty()) {
                    Event.Success(token)
                } else {
                    Event.Failure("Token is null")
                }
            }

            is Event.Failure -> {
                val error = event.exception
                Event.Failure(error)
            }
        }
    }


    override suspend fun fetchStepikToken(): Flow<String?> = flow {
        val token = prefsDataSource.fetchStepikToken()
        emit(token)
    }

    override suspend fun saveStepikToken(token: String) = prefsDataSource.saveStepikToken(token)
}