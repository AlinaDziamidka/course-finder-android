package com.example.coursefinderapp.presentation.account

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursefinderapp.databinding.FragmentAccountBinding
import com.example.coursefinderapp.domain.entity.Course
import com.example.coursefinderapp.presentation.account.adapter.AccountAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountView : Fragment() {

    private val viewModel: AccountViewModel by viewModels()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AccountAdapter
    private lateinit var coursesView: RecyclerView
    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
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
        shimmerLayout = binding.shimmerLayout
    }

    private fun initAdapter() {
        coursesView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = AccountAdapter(requireContext(), mutableListOf())

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
                        is AccountViewState.Success -> {
                            handleOnSuccess(it.data)
                        }

                        is AccountViewState.Loading -> {
                            delay(1000)
                            startShimmer(shimmerLayout, coursesView)
                        }

                        is AccountViewState.Failure -> {
                            handleOnFailure()
                        }
                    }
                }
            }
        }
    }

    private fun startShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        view.visibility = View.GONE
    }

    private fun handleOnSuccess(courses: List<Course>) {
        adapter.setCourses(courses.toMutableList())
        stopShimmer(shimmerLayout, coursesView)
    }

    private fun stopShimmer(shimmerLayout: ShimmerFrameLayout, view: View) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        view.visibility = View.VISIBLE
    }

    private fun handleOnFailure() {
        stopShimmer(shimmerLayout, coursesView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}