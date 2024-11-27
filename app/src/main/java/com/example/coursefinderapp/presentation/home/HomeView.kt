package com.example.coursefinderapp.presentation.home

import android.os.Bundle

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.coursefinderapp.R
import com.example.coursefinderapp.data.remote.worker.SyncWorker
import com.example.coursefinderapp.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeView : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent()
        createNavController()
        setBottomNavigation()
        onBackPressedAction()
        runWorker()
    }

    private fun setContent() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun createNavController() {
        val navHostFragment = setUpNavHostFragment()
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph_home)
    }

    private fun setUpNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment

    private fun setBottomNavigation() {
        setSelectedItem()
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeView -> navController.navigate(R.id.homeViewFragment)
                R.id.favoriteView -> navController.navigate(R.id.favoriteView)
                R.id.account -> navController.navigate(R.id.accountView)
            }
            true
        }
    }

    private fun setSelectedItem() {
        binding.bottomNavigationView.selectedItemId = R.id.homeView
    }

    private fun onBackPressedAction() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.navigateUp()) {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun runWorker() {
        val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            1, TimeUnit.DAYS
        )
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncCoursesWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}