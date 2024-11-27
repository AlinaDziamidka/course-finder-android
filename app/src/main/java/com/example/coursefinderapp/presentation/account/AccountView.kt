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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountView : Fragment() {

    private val viewModel: AccountViewModel by viewModels()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AccountAdapter
    private lateinit var coursesView: RecyclerView

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
                            Log.d(OBSERVE, "Success to fetch courses: ${it.data}")
                            handleOnSuccess(it.data)
                        }

                        is AccountViewState.Loading -> {
                            Log.d(OBSERVE, "Loading fetching courses")
                        }

                        is AccountViewState.Failure -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavoriteView"
        private const val OBSERVE = "coursesObserver"
    }

}