package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(): Flow<String?> = sessionRepository.fetchToken()
}