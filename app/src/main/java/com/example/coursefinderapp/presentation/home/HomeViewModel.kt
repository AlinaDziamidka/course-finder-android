package com.example.coursefinderapp.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
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

    private val _sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _filter = MutableStateFlow<Filters>(Filters.NoFilter)
    val filter: StateFlow<Filters> = _filter

    fun updateSortOrder(order: SortOrder) {
        Log.d("HomeViewModel", "Updating sort order to $order")
        _sortOrder.value = order
        fetchPagedCourses()
    }

    fun updateFilter(filter: Filters) {
        Log.d("HomeViewModel", "Updating filter to $filter")
        _filter.value = filter
        fetchPagedCourses()
    }

    fun fetchPagedCourses() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "fetchPagedCourses order: $_sortOrder.value")
            fetchCoursesUseCase.invoke(FetchCoursesUseCase.Params(_sortOrder.value, _filter.value))
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
