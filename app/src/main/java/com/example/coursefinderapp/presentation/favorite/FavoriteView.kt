package com.example.coursefinderapp.presentation.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursefinderapp.databinding.FragmentFavoriteBinding
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.util.DataSource
import com.example.coursefinderapp.presentation.favorite.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteView : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteAdapter
    private lateinit var coursesView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initAdapter()
        setCourses()
        observeCourses()
    }

    private fun initViews() {
        coursesView = binding.coursesRecyclerView
    }

    private fun initAdapter() {
        coursesView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = FavoriteAdapter(requireContext(), mutableListOf(),
            { course ->
                onFavoriteClick(course)
            },
            { course ->
                onDescriptionClick(course)
            })

        coursesView.adapter = adapter
    }

    private fun setCourses() {
        viewModel.setUpCourses()
    }


    private fun observeCourses() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is FavoriteViewState.Success -> {
                            Log.d(OBSERVE, "Success to fetch courses: ${it.data}")
                            handleOnSuccess(it.data)
                        }

                        is FavoriteViewState.Loading -> {
                            Log.d(OBSERVE, "Loading fetching courses")
                        }

                        is FavoriteViewState.Failure -> {
                            Log.d(OBSERVE, "Failed to fetch courses: ${it}.message}")
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess(courses: List<Course>) {
        adapter.setCourses(courses.toMutableList())
    }

    private fun onFavoriteClick(course: Course) {
        viewModel.deleteCourse(course)
        adapter.removeCourse(course)
    }

    private fun onDescriptionClick(course: Course) {
        val action =
            FavoriteViewDirections.actionFavoriteViewToCourseDetailsView(
                course.id,
                DataSource.DATABASE.stringValue
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavoriteView"
        private const val OBSERVE = "coursesObserver"
    }
}