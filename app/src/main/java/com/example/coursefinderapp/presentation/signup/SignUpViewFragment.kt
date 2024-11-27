package com.example.coursefinderapp.presentation.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.coursefinderapp.R
import com.example.coursefinderapp.databinding.FragmentSignUpBinding
import com.example.coursefinderapp.domain.usecase.SignUpUseCase
import com.example.coursefinderapp.domain.usecase.SignUpUseCase.SignUpFailure
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpViewFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var username: EditText
    private lateinit var userPassword: EditText
    private lateinit var userConfirmPassword: EditText
    private lateinit var signUpAction: Button
    private lateinit var signInAction: LinearLayout
    private lateinit var usernameErrorNotification: TextView
    private lateinit var passwordErrorNotification: TextView
    private lateinit var confirmPasswordErrorNotification: TextView
    private lateinit var progressOverlay: FrameLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpViews()
        observeEvents()
        setUpSignInAction()
    }

    private fun initViews() {
        signUpAction = binding.signUpContainer.signUpActionView
        username = binding.signUpContainer.emailContentView
        userPassword = binding.signUpContainer.passwordContentView
        userConfirmPassword = binding.signUpContainer.confirmPasswordContentView
        signInAction = binding.signUpContainer.signInContainer
        progressOverlay = binding.progressOverlay
        initErrorViews()
    }

    private fun initErrorViews() {
        usernameErrorNotification = binding.signUpContainer.invalidEmailView
        passwordErrorNotification = binding.signUpContainer.invalidPasswordView
        confirmPasswordErrorNotification = binding.signUpContainer.invalidConfirmPasswordView
    }

    private fun setUpViews() {
        signUpAction.setOnClickListener {
            clearErrorNotifications()

            viewModel.onSignUpButtonClicked(
                password = userPassword.text.toString(),
                username = username.text.toString(),
                confirmPassword = userConfirmPassword.text.toString()
            )
        }
    }

    private fun clearErrorNotifications() {
        usernameErrorNotification.visibility = View.GONE
        confirmPasswordErrorNotification.visibility = View.GONE
        passwordErrorNotification.visibility = View.GONE
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signUpFailure.collect { failure ->
                    failure?.let { handleSignUpFailure(failure) }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    when (it) {
                        is SignUpViewState.Success -> {
                            handleOnSuccess()
                        }

                        is SignUpViewState.Loading -> {
                            signUpAction.isEnabled = false
                            progressOverlay.visibility + View.VISIBLE
                        }

                        is SignUpViewState.Failure -> {
                            signUpAction.isEnabled = true
                            progressOverlay.visibility = View.GONE
                        }

                        is SignUpViewState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun handleOnSuccess() {
        moveToHomeScreen()
        progressOverlay.visibility + View.GONE
    }

    private fun handleSignUpFailure(failure: SignUpFailure) {
        when (failure) {
            is SignUpFailure.UsernameExistFailure -> {
                showUsernameExistFailure()
            }

            is SignUpFailure.UserNameFailure -> {
                showUsernameFailure()
            }

            is SignUpFailure.PasswordFailure -> {
                showPasswordFailure()
            }

            is SignUpFailure.ConfirmPasswordFailure -> {
                showConfirmPasswordFailure()
            }

            is SignUpFailure.EmptyUsernameFailure -> {
                showEmptyUsernameFailure()
            }

            is SignUpFailure.EmptyConfirmPasswordFailure -> {
                showEmptyConfirmPasswordFailure()
            }
        }
    }

    private fun showUsernameExistFailure() {
        usernameErrorNotification.setText(R.string.sign_up_invalid_username)
        usernameErrorNotification.visibility = View.VISIBLE
    }

    private fun showUsernameFailure() {
        usernameErrorNotification.setText(R.string.sign_up_invalid_email)
        usernameErrorNotification.visibility = View.VISIBLE
    }

    private fun showPasswordFailure() {
        confirmPasswordErrorNotification.setText(R.string.sign_up_password_requirements)
        passwordErrorNotification.visibility = View.VISIBLE
    }

    private fun showConfirmPasswordFailure() {
        usernameErrorNotification.setText(R.string.sign_up_invalid_password)
        confirmPasswordErrorNotification.visibility = View.VISIBLE
    }

    private fun showEmptyUsernameFailure() {
        usernameErrorNotification.setText(R.string.sign_up_empty_field)
        usernameErrorNotification.visibility = View.VISIBLE
    }

    private fun showEmptyConfirmPasswordFailure() {
        confirmPasswordErrorNotification.setText(R.string.sign_up_empty_field)
        confirmPasswordErrorNotification.visibility = View.VISIBLE
    }

    private fun moveToHomeScreen() {
        val action = SignUpViewFragmentDirections.actionSignUpViewFragmentToHomeView()
        findNavController().navigate(action)
    }

    private fun setUpSignInAction() {
        signInAction.setOnClickListener {
            moveToSignInScreen()
        }
    }

    private fun moveToSignInScreen() {
        val action = SignUpViewFragmentDirections.actionSignUpViewFragmentToSignInView()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}