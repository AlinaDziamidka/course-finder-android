package com.example.coursefinderapp.domain.util

import ApiError

sealed class Event<out T : Any> {
    data class Success<out T : Any>(val data: T) : Event<T>()
    data class Failure(val exception: String) : Event<Nothing>()
}

    sealed class EventDomain<out T, out E> {
        class Success<T>(val data: T) : EventDomain<T, Nothing>()
        class Failure<E>(val failure: E) : EventDomain<Nothing, E>()
}