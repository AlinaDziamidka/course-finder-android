package com.example.coursefinderapp.domain.usecase


import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.repository.remote.StepikAuthRepository
import com.example.coursefinderapp.domain.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchStepikTokenUseCase @Inject constructor(private val stepikAuthRepository: StepikAuthRepository) {

    suspend operator fun invoke() {
        val tokenFlow = stepikAuthRepository.fetchStepikToken()
        val stepikToken = tokenFlow.firstOrNull()

        if (stepikToken.isNullOrEmpty()) {
            val event = stepikAuthRepository.getStepikToken()
            when (event) {
                is Event.Success -> {
                    val token = event.data
                    withContext(Dispatchers.IO) {
                        stepikAuthRepository.saveStepikToken(token)
                    }
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        }
    }
}