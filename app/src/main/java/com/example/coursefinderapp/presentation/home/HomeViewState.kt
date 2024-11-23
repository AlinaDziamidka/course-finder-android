package com.example.coursefinderapp.presentation.home

interface HomeViewState<out T> {
    data class Success<T>(val data: T) : HomeViewState<T>
    data class Failure(val message: String) : HomeViewState<Nothing>
    data object Loading : HomeViewState<Nothing>
}