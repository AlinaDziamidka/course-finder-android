package com.example.coursefinderapp.data.remote.repository

import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.CLIENT_ID_STEPIK
import com.example.coursefinderapp.data.remote.api.NetworkClientConfig.CLIENT_SECRET_STEPIK
import com.example.coursefinderapp.data.remote.api.service.StepikApiService
import com.example.coursefinderapp.data.remote.prefs.PrefsDataSource
import com.example.coursefinderapp.domain.repository.remote.StepikAuthRepository
import com.example.coursefinderapp.domain.util.Event
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
        val response = stepikApiService.getToken(credentials)

        return if (response.isSuccessful) {
            val token = response.body()?.accessToken
            if (token != null) {
                Event.Success(token)
            } else {
                Event.Failure("Token is null")
            }
        } else {
            Event.Failure("Failed to get token: ${response.message()}")
        }
    }

    override suspend fun fetchStepikToken(): Flow<String?> = flow {
        val token = prefsDataSource.fetchStepikToken()
        emit(token)
    }

    override suspend fun saveStepikToken(token: String) = prefsDataSource.saveStepikToken(token)
}