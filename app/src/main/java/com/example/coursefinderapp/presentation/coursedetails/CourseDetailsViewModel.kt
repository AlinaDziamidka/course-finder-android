package com.example.coursefinderapp.presentation.coursedetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.usecase.FetchAuthorUseCase
import com.example.coursefinderapp.domain.usecase.FetchCourseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    context: Application,
    private val fetchCourseUseCase: FetchCourseUseCase,
    private val fetchAuthorUseCase: FetchAuthorUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<CourseDetailsViewState<Course>>(CourseDetailsViewState.Loading)
    val viewState: StateFlow<CourseDetailsViewState<Course>> =
        _viewState

    private val _authorViewState =
        MutableStateFlow<CourseDetailsViewState<Author>>(CourseDetailsViewState.Loading)
    val authorViewState: StateFlow<CourseDetailsViewState<Author>> =
        _authorViewState

    fun setUpCourse(courseId: Int, dataSource: String) {
        viewModelScope.launch {
            fetchCourseUseCase.invoke(FetchCourseUseCase.Params(courseId, dataSource))
                .onStart {
                    _viewState.value = CourseDetailsViewState.Loading
                }
                .catch { exception ->
                    _viewState.value =
                        CourseDetailsViewState.Failure(exception.message ?: "Something went wrong")
                }
                .collect {
                    _viewState.value = CourseDetailsViewState.Success(it)
                }
        }
    }

    fun setUpAuthor(course: Course) {
        viewModelScope.launch {
            fetchAuthorUseCase.invoke(FetchAuthorUseCase.Params(course))
                .onStart {
                    _authorViewState.value = CourseDetailsViewState.Loading
                }
                .catch { exception ->
                    _authorViewState.value =
                        CourseDetailsViewState.Failure(exception.message ?: "Something went wrong")
                }
                .collect {
                    _authorViewState.value = CourseDetailsViewState.Success(it)
                }
        }
    }
}