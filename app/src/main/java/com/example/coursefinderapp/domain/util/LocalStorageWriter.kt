package com.example.coursefinderapp.domain.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> insertToLocalDatabase(insertFunction: suspend (T) -> Unit, data: T) =
    withContext(Dispatchers.IO) {
        insertFunction(data)
    }

suspend fun <K, V> insertToCache(insertFunction: suspend (K, V) -> Unit, key: K, value: V) =
    withContext(Dispatchers.IO) {
    insertFunction(key, value)
}