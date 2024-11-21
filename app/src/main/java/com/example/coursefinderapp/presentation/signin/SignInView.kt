package com.example.coursefinderapp.presentation.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.coursefinderapp.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInView : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var signInAction: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var progressOverlay: FrameLayout
    private lateinit var failureNotification: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpViews()
        observeEvents()
        signUpAction()
    }

    private fun initViews() {
        signInAction = binding.signInContainer.signInActionView
        username = binding.signInContainer.emailContentView
        password = binding.signInContainer.passwordContentView
        progressOverlay = binding.progressOverlay
        failureNotification = binding.signInContainer.invalidEmailOrPasswordView
    }

    private fun setUpViews() {
        Log.d("SignInView", "setUpViews: Setting up button click listener")
        signInAction.setOnClickListener {
            viewModel.onSignInButtonClicked(
                username = username.text.toString(),
                password = password.text.toString(),
            )
            Log.d("SignInView", "Sign-in button clicked with username: $username and password: $password")
        }
    }

    private fun observeEvents() {
        Log.d("SignInView", "observeEvents: Observing view state")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    Log.d("SignInView", "View state updated: $it")
                    when (it) {
                        is SignInViewState.Success -> {
                            Log.d("SignInView", "Sign-in successful")
                            handleOnSuccess()
                        }

                        is SignInViewState.Loading -> {
                            Log.d("SignInView", "Loading state: Showing progress overlay")
                            progressOverlay.visibility = View.VISIBLE
                            signInAction.isEnabled = false
                        }

                        is SignInViewState.Failure -> {
                            Log.d("SignInView", "Sign-in failed: Handling failure")
                            handleOnFailure()
                        }

                        is SignInViewState.Idle -> {
                            Log.d("SignInView", "Idle state")
                        }
                    }
                }
            }
        }
    }

    private fun handleOnSuccess() {
        Log.d("SignInView", "handleOnSuccess: Navigating to home screen")
        moveToHomeScreen()
        progressOverlay.visibility = View.GONE
    }

    private fun handleOnFailure() {
        Log.d("SignInView", "handleOnFailure: Showing failure notification")
        signInAction.isEnabled = true
        progressOverlay.visibility = View.GONE
        showFailureNotification()
    }

    private fun showFailureNotification() {
        failureNotification.visibility = View.VISIBLE
    }

    private fun moveToHomeScreen() {
        Log.d("SignInView", "moveToHomeScreen: Navigate to home screen logic")
        val action = SignInViewDirections.actionSignInViewToHomeView()
        findNavController().navigate(action)
    }

    private fun signUpAction() {
        binding.signInContainer.signUpView.setOnClickListener {
            moveToSignUpScreen()
        }
    }

    private fun moveToSignUpScreen() {
        val action = SignInViewDirections.actionSignInViewToSignUpViewFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}