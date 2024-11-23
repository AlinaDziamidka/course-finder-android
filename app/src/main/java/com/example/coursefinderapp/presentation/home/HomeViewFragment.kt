package com.example.coursefinderapp.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.FragmentHomeBinding
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.StepikMeta
import com.example.coursefinderapp.presentation.home.adapter.CourseLoadStateAdapter
import com.example.coursefinderapp.presentation.home.adapter.CoursesAdapter
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeViewFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CoursesAdapter
    private lateinit var coursesView: RecyclerView
    private lateinit var filterActin: MaterialButton
    private lateinit var selectedFilter: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setUpCourses()
        observeCourses()
    }

    private fun initViews() {
        coursesView = binding.coursesRecyclerView
        filterActin = binding.filterActionView
        selectedFilter = binding.filterViewContainer
    }

    private fun setUpCourses() {
        viewModel.fetchPagedCourses()
    }

    private fun observeCourses() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is HomeViewState.Success -> {
                            Log.d(OBSERVE, "Success to fetch courses: ${it.data}")
                            handleOnSuccess(it.data)
                        }

                        is HomeViewState.Loading -> {
                            Log.d(OBSERVE, "Loading fetching courses")
                        }

                        is HomeViewState.Failure -> {
                            Log.d(OBSERVE, "Failed to fetch courses: ${it.message}")
                        }
                    }

                }
            }
        }
    }

    private suspend fun handleOnSuccess(courses: PagingData<Course>) {
        adapter.submitData(courses)
    }

    private fun onDescriptionClick(course: Course) {
        Toast.makeText(requireContext(), "Описание курса: ${course.title}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun onFavoriteClick(course: Course) {
        Toast.makeText(
            requireContext(),
            "Добавить в избранное: ${course.title}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initAdapter() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        coursesView.layoutManager = layoutManager

        adapter = CoursesAdapter(requireContext(),
            { course ->
                onDescriptionClick(course)
            },
            { course ->
                onFavoriteClick(course)
            })
        coursesView.adapter = adapter

        binding.coursesRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = CourseLoadStateAdapter { adapter.retry() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HomeViewFragment"
        const val OBSERVE = "coursesObserver"
    }
}


