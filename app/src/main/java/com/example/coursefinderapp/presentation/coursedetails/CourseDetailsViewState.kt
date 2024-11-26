package com.example.coursefinderapp.presentation.coursedetails

interface CourseDetailsViewState <out T> {

    data class Success<T>(val data: T) : CourseDetailsViewState<T>
    data class Failure(val message: String) : CourseDetailsViewState<Nothing>
    data object Loading : CourseDetailsViewState<Nothing>
}