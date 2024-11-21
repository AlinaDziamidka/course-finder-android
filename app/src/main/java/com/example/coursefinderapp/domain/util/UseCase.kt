package com.example.coursefinderapp.domain.util

import kotlinx.coroutines.flow.Flow

interface UseCase<T, R> {
    suspend operator fun invoke(params: T): Flow<R>

    class None
}