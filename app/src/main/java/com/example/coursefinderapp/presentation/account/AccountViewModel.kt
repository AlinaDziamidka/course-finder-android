package com.example.coursefinderapp.presentation.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.usecase.FetchStartedCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    context: Application,
    private val fetchStartedCoursesUseCase: FetchStartedCoursesUseCase
) : AndroidViewModel(context) {

    private val _viewState =
        MutableStateFlow<AccountViewState<List<Course>>>(AccountViewState.Loading)
    val viewState: StateFlow<AccountViewState<List<Course>>> =
        _viewState

    fun setUpCourses() {
        viewModelScope.launch {
            fetchStartedCoursesUseCase(Unit)
                .onStart {
                    _viewState.value = AccountViewState.Loading
                }.catch {
                    _viewState.value =
                        AccountViewState.Failure(it.message ?: "Something went wrong.")
                }.collect {
                    _viewState.value = AccountViewState.Success(it)
                }
        }
    }

}