package com.example.coursefinderapp.domain.usecase

import ApiError
import com.example.coursefinderapp.domain.entity.Session
import com.example.coursefinderapp.domain.repository.remote.SessionRepository
import com.example.coursefinderapp.domain.util.Event
import com.example.coursefinderapp.domain.util.EventDomain
import com.example.coursefinderapp.domain.util.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import toApiError
import javax.inject.Inject

typealias SignUpUseCaseEvent = EventDomain<Session, SignUpUseCase.SignUpFailure>

class SignUpUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) : UseCase<SignUpUseCase.Params, SignUpUseCaseEvent> {

    data class Params(
        val password: String,
        val username: String,
        val confirmPassword: String
    )

    sealed class SignUpFailure {
        data object UsernameExistFailure : SignUpFailure()
        data object UserNameFailure : SignUpFailure()
        data object PasswordFailure : SignUpFailure()
        data object ConfirmPasswordFailure : SignUpFailure()
        data object EmptyUsernameFailure : SignUpFailure()
        data object EmptyConfirmPasswordFailure : SignUpFailure()
    }

    override suspend operator fun invoke(params: Params): Flow<SignUpUseCaseEvent> = flow {
        val password = params.password
        val username = params.username

        when (val error = validate(params)) {
            is SignUpFailure -> emit(EventDomain.Failure(error))
            null -> {
                val event = sessionRepository.signUp(
                    password = password, username = username
                )

                when (event) {
                    is Event.Success -> {
                        val session = event.data
                        sessionRepository.saveToken(session.token)
                        sessionRepository.saveUserProfile(session.userProfile)
                        emit(EventDomain.Success(session))
                    }

                    is Event.Failure -> {
                        val exception = event.exception
                        when (ApiErrorCode.fromMessage(exception)) {
                            ApiErrorCode.EMAIL_EXISTS -> {
                                emit(EventDomain.Failure(SignUpFailure.UsernameExistFailure))
                            }
                            else -> {
                                throw Exception(exception)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun validate(params: Params): SignUpFailure? {

        return when {
            params.username.isEmpty() -> SignUpFailure.EmptyUsernameFailure
            !isValidEmail(params.username) -> SignUpFailure.UserNameFailure
            params.confirmPassword.isEmpty() -> SignUpFailure.EmptyConfirmPasswordFailure
            !isValidPassword(params.password) -> SignUpFailure.PasswordFailure
            params.password != params.confirmPassword -> SignUpFailure.ConfirmPasswordFailure
            else -> null
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val digitRegex = Regex("\\d")
        val lowerCaseRegex = Regex("[a-z]")
        val upperCaseRegex = Regex("[A-Z]")

        return password.length >= 8 && digitRegex.containsMatchIn(password) && lowerCaseRegex.containsMatchIn(
            password
        ) && upperCaseRegex.containsMatchIn(password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return emailRegex.matches(email)
    }
}