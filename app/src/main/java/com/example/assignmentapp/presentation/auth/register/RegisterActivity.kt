package com.example.assignmentapp.presentation.auth.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignmentapp.R
import com.example.assignmentapp.databinding.ActivityRegisterBinding
import com.example.assignmentapp.presentation.auth.login.LoginActivity
import com.example.assignmentapp.presentation.core.BaseActivity
import com.example.assignmentapp.presentation.extention.collectLatestLifeCycleFlow
import com.example.assignmentapp.presentation.extention.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_register)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    private fun init() {
        bindUi()
        setUpObservers()
        setUpListeners()
    }


    private fun bindUi() {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
    }

    private fun setUpObservers() {
        this.collectLatestLifeCycleFlow(viewModel.errorMessage) {
            showMessageDialogWithOkAction(it)
        }

        this.collectLatestLifeCycleFlow(viewModel.isLoading) { isLoading ->
            if (isLoading) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        this.collectLatestLifeCycleFlow(viewModel.validateRegisterState) { validateRegisterState ->
            handleValidateRegisterState(validateRegisterState)
        }
    }

    private fun setUpListeners() {
        binding.btnSignUp.setOnClickListener {
            signUp(
                binding.etFirstName.text.toString().trim(),
                binding.etLastName.text.toString().trim(),
                binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString().trim(),
                binding.etConfirmPassword.text.toString().trim()
            )

        }
    }


    private fun handleValidateRegisterState(validateRegisterState: RegisterViewModel.ValidateRegisterStates) {
        var validationMessageString: String? = null

        when (validateRegisterState) {

            RegisterViewModel.ValidateRegisterStates.STATE_EMPTY_FIELD -> {
                validationMessageString =
                    getString(R.string.msg_empty_field)
            }

            RegisterViewModel.ValidateRegisterStates.STATE_VALID_FORM -> {
                showSuccessMsg()
            }

            RegisterViewModel.ValidateRegisterStates.STATE_MISMATCH_PASSWORD -> {
                validationMessageString =
                    getString(R.string.msg_password_mismatch)
            }
        }

        validationMessageString.let {
            showMessageDialogWithOkAction(messageContent = validationMessageString)
        }

    }

    private fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModel.validateAndSignUp(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
    }

    private fun showSuccessMsg() {
        showMessageDialogWithOkAction(getString(R.string.msg_success), okAction = {
            startActivity<LoginActivity> { }
        })
    }


}