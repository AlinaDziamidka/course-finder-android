package com.example.coursefinderapp.presentation.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpView : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent()
        createNavController()
    }

    private fun createNavController() {
        val navHostFragment = setUpNavHostFragment()
        navController = navHostFragment.navController
    }

    private fun setContent() {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
}
