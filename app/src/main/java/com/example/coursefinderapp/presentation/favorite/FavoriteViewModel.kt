package com.example.coursefinderapp.presentation.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.usecase.DeleteFavoriteCourseUseCase
import com.example.coursefinderapp.domain.usecase.FetchFavoriteCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    context: Application,
    private val fetchFavoriteCoursesUseCase: FetchFavoriteCoursesUseCase,
    private val deleteFavoriteCourseUseCase: DeleteFavoriteCourseUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<FavoriteViewState<List<Course>>>(FavoriteViewState.Loading)
    val viewState: StateFlow<FavoriteViewState<List<Course>>> =
        _viewState

    fun setUpCourses() {
        viewModelScope.launch {
            fetchFavoriteCoursesUseCase(Unit)
                .onStart {
                    _viewState.value = FavoriteViewState.Loading
                }.catch {
                    _viewState.value =
                        FavoriteViewState.Failure(it.message ?: "Something went wrong.")
                }.collect {
                    _viewState.value = FavoriteViewState.Success(it)
                }
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            runCatching {
                deleteFavoriteCourseUseCase(
                    DeleteFavoriteCourseUseCase.Params(course)
                )
            }.onSuccess {
                Log.e(TAG, "Course deleted successfully")
            }.onFailure { e ->
                Log.e(TAG, "Error deleting course: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "FavoriteViewModel"
    }
}