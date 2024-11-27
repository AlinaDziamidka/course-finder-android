package com.example.coursefinderapp.presentation.account


interface AccountViewState <out T> {

    data class Success<T>(val data: T) : AccountViewState<T>
    data class Failure(val message: String) : AccountViewState<Nothing>
    data object Loading : AccountViewState<Nothing>
}