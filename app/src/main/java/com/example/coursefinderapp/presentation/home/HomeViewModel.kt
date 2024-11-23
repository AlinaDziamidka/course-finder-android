package com.example.coursefinderapp.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.usecase.FetchCoursesUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Application,
    private val fetchCoursesUseCase: FetchCoursesUseCase
) : AndroidViewModel(context) {

    private val _pagedCoursesFlow = MutableStateFlow<PagingData<Course>>(PagingData.empty())
    val pagedCoursesFlow: StateFlow<PagingData<Course>> get() = _pagedCoursesFlow

    private val _viewState =
        MutableStateFlow<HomeViewState<PagingData<Course>>>(HomeViewState.Loading)
    val viewState: StateFlow<HomeViewState<PagingData<Course>>> = _viewState

    fun fetchPagedCourses() {
        viewModelScope.launch {
            fetchCoursesUseCase.invoke(Unit)
                .onStart {
                    _viewState.value = HomeViewState.Loading
                }
                .catch { exception ->
                    _viewState.value =
                        HomeViewState.Failure(exception.message ?: "Something went wrong")
                }
                .collect { pagingData ->
                    _viewState.value = HomeViewState.Success(pagingData)
                    _pagedCoursesFlow.value = pagingData
                }
        }
    }
}
