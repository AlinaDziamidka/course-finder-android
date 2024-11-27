package com.example.coursefinderapp.presentation.coursedetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursefinderapp.domain.entity.Author
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.usecase.FetchAuthorUseCase
import com.example.coursefinderapp.domain.usecase.FetchCourseUseCase
import com.example.coursefinderapp.domain.usecase.SaveFavoriteCourseUseCase
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
    private val fetchAuthorUseCase: FetchAuthorUseCase,
    private val saveFavoriteCourseUseCase: SaveFavoriteCourseUseCase
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

    fun setUpAuthor(course: Course, dataSource: String) {
        viewModelScope.launch {
            fetchAuthorUseCase.invoke(FetchAuthorUseCase.Params(course, dataSource))
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

    fun saveCourseToFavorite(course: Course) {
        viewModelScope.launch {
            runCatching {
                saveFavoriteCourseUseCase(
                    SaveFavoriteCourseUseCase.Params(course)
                )
            }.onSuccess {
                Log.d(TAG, "Course added to favorite successfully")
            }.onFailure { e ->
                Log.e(TAG, "Error saving course to favorite: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "CourseDetailsViewModel"
    }
}