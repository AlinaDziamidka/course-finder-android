package com.example.coursefinderapp.presentation.favorite

interface FavoriteViewState<out T> {

    data class Success<T>(val data: T) : FavoriteViewState<T>
    data class Failure(val message: String) : FavoriteViewState<Nothing>
    data object Loading : FavoriteViewState<Nothing>
}