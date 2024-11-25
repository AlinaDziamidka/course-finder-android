package com.example.coursefinderapp.domain.entity

sealed class Filters {

    data class Category(val type: Type) : Filters() {
        enum class Type(val code: Int) {
            PYTHON(3),
            C_SHARP(4),
            C(5),
            JAVA(6),
            KOTLIN(8)
        }
    }

    data class Price(val type: Type) : Filters() {
        enum class Type {
            FREE,
            PAID
        }
    }

    data object NoFilter : Filters()

    companion object {
        private const val PYTHON_TAG = 3
        private const val C_SHARP_TAG = 4
        private const val C_TAG = 5
        private const val JAVA_TAG = 6
        private const val KOTLIN_TAG = 8
    }
}