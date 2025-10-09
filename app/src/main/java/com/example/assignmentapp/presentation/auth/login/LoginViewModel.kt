package com.example.assignmentapp.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapp.data.Resource
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
class LoginViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _validateLoginState = MutableSharedFlow<ValidateBookingStates>()
    val validateLoginState = _validateLoginState.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableSharedFlow<Boolean>()
    val isLoggedIn = _isLoggedIn.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {

            if (email.isBlank() || password.isBlank()) {
                _validateLoginState.emit(ValidateBookingStates.STATE_EMPTY_FIELD)
                return@launch
            }

            _isLoading.value = true
            try {
                when (val result = userRepository.login(email, password)) {
                    is Resource.Success -> {
                        _validateLoginState.emit(ValidateBookingStates.STATE_VALID_FORM)
                    }

                    is Resource.Error -> {
                        _errorMessage.emit(result.exception.message.toString())
                    }

                    Resource.Loading -> {
                    }
                }
            } catch (e: Exception) {
                _errorMessage.emit(e.message ?: "Something went wrong")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isLoggedIn() {
        viewModelScope.launch {
            val response = userRepository.isLoggedIn()
            _isLoggedIn.emit(response)
        }
    }

    enum class ValidateBookingStates {
        STATE_EMPTY_FIELD, STATE_VALID_FORM
    }
}