package com.example.assignmentapp.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.User
import com.example.assignmentapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _validateRegisterState = MutableSharedFlow<ValidateRegisterStates>()
    val validateRegisterState = _validateRegisterState.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    fun validateAndSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                _validateRegisterState.emit(ValidateRegisterStates.STATE_EMPTY_FIELD)
                return@launch
            }

            if (password != confirmPassword) {
                _validateRegisterState.emit(ValidateRegisterStates.STATE_MISMATCH_PASSWORD)
                return@launch
            }

            signUp(firstName, lastName, email, password)
        }
    }

    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        val result = userRepository.insertUser(User(firstName, lastName, email, password))
        when (result) {
            is Resource.Error -> {
                _errorMessage.emit(result.exception.message.toString())
            }

            Resource.Loading -> TODO()
            is Resource.Success<*> -> {
                _validateRegisterState.emit(ValidateRegisterStates.STATE_VALID_FORM)
            }
        }

    }


    enum class ValidateRegisterStates {
        STATE_EMPTY_FIELD, STATE_VALID_FORM, STATE_MISMATCH_PASSWORD
    }

}