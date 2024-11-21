package com.example.coursefinderapp.presentation.onboarding

interface OnboardingViewState {
    data object Success : OnboardingViewState
    data object Failure : OnboardingViewState
    data object Loading : OnboardingViewState
}