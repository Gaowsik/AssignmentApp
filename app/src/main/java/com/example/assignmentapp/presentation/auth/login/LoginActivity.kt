package com.example.assignmentapp.presentation.auth.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignmentapp.MainActivity
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.ActivityLoginBinding
import com.example.assignmentapp.presentation.auth.register.RegisterActivity
import com.example.assignmentapp.presentation.core.BaseActivity
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import com.example.assignmentapp.presentation.extention.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    private fun init() {
        bindUi()
        setUpObservers()
        checkLoginStatus()
        setUpListener()
    }

    private fun setUpListener() {
        binding.btnLogin.setOnClickListener {
            login(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }

        binding.tvSignUp.setOnClickListener {
            startActivity<RegisterActivity>() {}
        }
    }

    private fun bindUi() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
    }

    private fun setUpObservers() {
        this.collectLatestLifeCycleFlow(viewModel.errorMessage) {
            showMessageDialogWithOkAction(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.isLoggedIn) { isLoggedIn ->
            handleIsUserAlreadyLoggedIn(isLoggedIn)
        }


        this.collectLatestLifeCycleFlow(viewModel.isLoading) { isLoading ->
            if (isLoading) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        this.collectLatestLifeCycleFlow(viewModel.validateLoginState) { validateLoginState ->
            handleValidateLoginState(validateLoginState)
        }
    }


    private fun handleValidateLoginState(validateLoginState: LoginViewModel.ValidateBookingStates) {
        var validationMessageString: String? = null

        when (validateLoginState) {
            LoginViewModel.ValidateBookingStates.STATE_EMPTY_FIELD -> {
                validationMessageString =
                    getString(R.string.msg_empty_field)
            }

            LoginViewModel.ValidateBookingStates.STATE_VALID_FORM -> {

            }
        }

        validationMessageString.let {
            showMessageDialogWithOkAction(messageContent = validationMessageString)
        }

    }


    private fun handleIsUserAlreadyLoggedIn(loggedIn: Boolean) {
        if (loggedIn) {
            startActivity<MainActivity> {
                finish()
            }
        }
    }


    private fun checkLoginStatus() {
        viewModel.isLoggedIn()
    }

    private fun login(email: String, password: String) {
        viewModel.login(email = email, password = password)
    }
}