package com.example.coursefinderapp.domain.usecase

import com.example.coursefinderapp.domain.entity.Session
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.util.Event
import com.example.coursefinderapp.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val sessionRepository: SessionRepository) :
    UseCase<SignInUseCase.Params, Session> {

    data class Params(
        val username: String,
        val password: String
    )

    override suspend operator fun invoke(params: Params): Flow<Session> = flow {
        val username = params.username
        val password = params.password
        if (username.isNotEmpty() && password.isNotEmpty()) {
            val event = sessionRepository.signIn(username = username, password = password)
            when (event) {
                is Event.Success -> {
                    val session = event.data
                    sessionRepository.saveToken(session.token)
                    sessionRepository.saveUserProfile(session.userProfile)
                    emit(session)
                }

                is Event.Failure -> {
                    throw Exception(event.exception)
                }
            }
        } else {
            throw Exception("Email or password is wrong.")
        }
    }
}