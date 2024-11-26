package com.example.coursefinderapp.domain.util

interface NonReturningUseCase<T> {

    suspend operator fun invoke(params: T)

    object None
}