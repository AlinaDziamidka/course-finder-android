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
import com.example.coursefinderapp.domain.usecase.SaveCourseToCacheUseCase
import com.example.coursefinderapp.domain.usecase.SaveFavoriteCourseUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Application,
    private val fetchCoursesUseCase: FetchCoursesUseCase,
    private val saveCourseToCacheUseCase: SaveCourseToCacheUseCase,
    private val saveFavoriteCourseUseCase: SaveFavoriteCourseUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<HomeViewState<PagingData<Course>>>(HomeViewState.Loading)
    val viewState: StateFlow<HomeViewState<PagingData<Course>>> = _viewState

    private val _sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _filter = MutableStateFlow<Filters>(Filters.NoFilter)
    val filter: StateFlow<Filters> = _filter

    fun updateSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun updateFilter(filter: Filters) {
        _filter.value = filter
    }

    val pagedCoursesFlow: Flow<PagingData<Course>> =
        combine(_sortOrder, _filter) { sortOrder, filter ->
            FetchCoursesUseCase.Params(sortOrder, filter)
        }
            .flatMapLatest { params ->
                fetchCoursesUseCase.invoke(params)
                    .onStart {
                        _viewState.value = HomeViewState.Loading
                    }
                    .catch { exception ->
                        _viewState.value =
                            HomeViewState.Failure(exception.message ?: "Something went wrong")
                        emit(PagingData.empty())
                    }
            }
            .onEach { pagingData ->
                _viewState.value = HomeViewState.Success(pagingData)
            }
            .cachedIn(viewModelScope)

    fun saveCourseToCache(course: Course) {
        viewModelScope.launch {
            runCatching {
                saveCourseToCacheUseCase(
                    SaveCourseToCacheUseCase.Params(course)
                )
            }.onSuccess {
                Log.d(TAG, "Course saved to cache successfully")
            }.onFailure { e ->
                Log.e(TAG, "Error saving course to cache: ${e.message}")
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
       private const val TAG = "HomeViewModel"
    }
}
