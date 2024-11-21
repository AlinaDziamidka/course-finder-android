package com.example.coursefinderapp.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.coursefinderapp.R
import com.example.coursefinderapp.data.remote.worker.FetchStepikTokenWorker
import com.example.coursefinderapp.databinding.ActivityOnboardingBinding
import com.example.coursefinderapp.domain.usecase.FetchStepikTokenUseCase
import com.example.coursefinderapp.presentation.home.HomeView
import com.example.coursefinderapp.presentation.signin.SignInView
import com.example.coursefinderapp.presentation.signup.SignUpView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingView : AppCompatActivity(R.layout.activity_onboarding) {

    private val viewModel: OnboardingViewModel by viewModels()
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
        observeOnboardingState()
        setOnContinueClick()
        runWorker()
    }

    private fun setContent() {
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun observeOnboardingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        OnboardingViewState.Success -> {
                            val intent = Intent(this@OnboardingView, HomeView::class.java)
                            startActivity(intent)
                            finish()
                        }

                        OnboardingViewState.Failure -> {
                        }

                        OnboardingViewState.Loading -> {
                        }
                    }
                }
            }
        }
    }

    private fun setOnContinueClick() {
        binding.continueAction.setOnClickListener {
            val intent = Intent(this@OnboardingView, SignUpView::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun runWorker() {
        val workRequest = OneTimeWorkRequestBuilder<FetchStepikTokenWorker>()
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }
}