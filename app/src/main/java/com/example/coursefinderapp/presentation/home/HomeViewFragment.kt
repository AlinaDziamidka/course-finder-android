package com.example.coursefinderapp.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.FragmentHomeBinding
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.domain.entity.Filters
import com.example.coursefinderapp.domain.entity.SortOrder
import com.example.coursefinderapp.domain.util.DataSource
import com.example.coursefinderapp.presentation.home.adapter.CourseLoadStateAdapter
import com.example.coursefinderapp.presentation.home.adapter.CoursesAdapter
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeViewFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CoursesAdapter
    private lateinit var coursesView: RecyclerView
    private lateinit var filterActin: MaterialButton
    private lateinit var sortAction: TextView

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
        observeCourses()
        setUpSortAction()
        setUpFilterAction()
    }

    private fun initViews() {
        coursesView = binding.coursesRecyclerView
        filterActin = binding.filterActionView
        sortAction = binding.sortNameView
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

    private fun observeCourses() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(viewModel.pagedCoursesFlow, viewModel.viewState) { pagingData, state ->
                    Pair(pagingData, state)
                }.collectLatest { (pagingData, state) ->
                    handleOnSuccess(pagingData)
                    when (state) {
                        is HomeViewState.Success -> {
                            Log.d(OBSERVE, "Success to fetch courses: ${state.data}")
                        }

                        is HomeViewState.Loading -> {
                            Log.d(OBSERVE, "Loading fetching courses")
                        }

                        is HomeViewState.Failure -> {
                            Log.d(OBSERVE, "Failed to fetch courses: ${state.message}")
                        }
                    }
                }
            }
        }
    }

    private suspend fun handleOnSuccess(courses: PagingData<Course>) {
        adapter.submitData(courses)
    }

    private fun setUpSortAction() {
        sortAction.setOnClickListener {
            showSortMenu(it)
        }
    }

    private fun showSortMenu(view: View) {
        val sortMenu = PopupMenu(requireContext(), view)
        sortMenu.inflate(R.menu.sort_menu)

        sortMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sortByDate -> {
                    viewModel.updateSortOrder(SortOrder.BY_DATE)
                    sortAction.text = getString(R.string.home_sort_menu_by_date)
                    true
                }

                R.id.sortByPopular -> {
                    viewModel.updateSortOrder(SortOrder.BY_POPULARITY)
                    sortAction.text = getString(R.string.home_sort_menu_by_popular)
                    true
                }

                R.id.sortByRating -> {
                    viewModel.updateSortOrder(SortOrder.BY_RATING)
                    sortAction.text = getString(R.string.home_sort_menu_by_rating)
                    true
                }

                else -> false
            }
        }

        sortMenu.show()
    }

    private fun setUpFilterAction() {
        filterActin.setOnClickListener {
            showFilterMenu(it)
        }
    }

    private fun showFilterMenu(view: View) {
        val filterMenu = PopupMenu(requireContext(), view)
        filterMenu.inflate(R.menu.filter_menu)

        filterMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.price -> {
                    true
                }

                R.id.free -> {
                    viewModel.updateFilter(Filters.Price(Filters.Price.Type.FREE))
                    true
                }

                R.id.paid -> {
                    viewModel.updateFilter(Filters.Price(Filters.Price.Type.PAID))
                    true
                }

                R.id.category -> {
                    true
                }

                R.id.python -> {
                    viewModel.updateFilter(Filters.Category(Filters.Category.Type.PYTHON))
                    true
                }

                R.id.cSharp -> {
                    viewModel.updateFilter(Filters.Category(Filters.Category.Type.C_SHARP))
                    true
                }

                R.id.c -> {
                    viewModel.updateFilter(Filters.Category(Filters.Category.Type.C))
                    true
                }

                R.id.java -> {
                    viewModel.updateFilter(Filters.Category(Filters.Category.Type.JAVA))
                    true
                }

                R.id.kotlin -> {
                    viewModel.updateFilter(Filters.Category(Filters.Category.Type.KOTLIN))
                    true
                }

                else -> false
            }
        }

        filterMenu.show()
    }

    private fun onDescriptionClick(course: Course) {
        viewModel.saveCourseToCache(course)
        val action =
            HomeViewFragmentDirections.actionHomeViewFragmentToCourseDetailsView(
                course.id,
                DataSource.CACHE.stringValue
            )
        findNavController().navigate(action)
    }

    private fun onFavoriteClick(course: Course) {
        viewModel.saveCourseToFavorite(course)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeViewFragment"
        const val OBSERVE = "coursesObserver"
    }
}


