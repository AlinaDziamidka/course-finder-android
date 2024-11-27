package com.example.coursefinderapp.domain.entity

sealed class Filters {

    data class Category(val type: Type) : Filters() {
        enum class Type(val code: Int) {
            PYTHON(PYTHON_TAG),
            C_SHARP(C_SHARP_TAG),
            C(C_TAG),
            JAVA(JAVA_TAG),
            KOTLIN(KOTLIN_TAG)
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