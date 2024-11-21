package com.example.coursefinderapp.domain.repository.remote

import com.example.coursefinderapp.domain.util.Event
import kotlinx.coroutines.flow.Flow

interface StepikAuthRepository {

    suspend fun getStepikToken(): Event<String>

    suspend fun fetchStepikToken(): Flow<String?>

    suspend fun saveStepikToken(token: String)
}