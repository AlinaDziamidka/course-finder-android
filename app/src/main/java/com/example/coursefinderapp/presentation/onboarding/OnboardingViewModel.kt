package com.example.coursefinderapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursefinderapp.domain.usecase.FetchSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val fetchSessionUseCase: FetchSessionUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<OnboardingViewState>(OnboardingViewState.Loading)
    val viewState: StateFlow<OnboardingViewState> = _viewState

    init {
        verifySession()
    }

    private fun verifySession() {
        viewModelScope.launch {
            fetchSessionUseCase()
                .onStart {}
                .catch {
                    _viewState.value =
                        OnboardingViewState.Failure
                }
                .collect { token ->
                    _viewState.value = if (token.isNullOrEmpty()) {
                        OnboardingViewState.Failure
                    } else {
                        OnboardingViewState.Success
                    }
                }
        }
    }
}