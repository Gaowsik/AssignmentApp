package com.example.assignmentapp.presentation.profile

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
class ProfileViewModel @Inject constructor(
    val userRepository: UserRepository
) : ViewModel() {


    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoggedOutSuccessful = MutableSharedFlow<Boolean>()
    val loggedOutSuccessful = _isLoggedOutSuccessful.asSharedFlow()


    fun getUser() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.getCurrentUser()
            when (result) {
                is Resource.Error -> {
                    _isLoading.value = false
                    _errorMessage.emit(result.exception.message.toString())
                }

                Resource.Loading -> TODO()
                is Resource.Success<*> -> {
                    _isLoading.value = false
                    _user.value = result.value as User
                }
            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            val result = userRepository.logout()
            when (result) {
                is Resource.Error -> {
                    _isLoading.value = false
                    _errorMessage.emit(result.exception.message.toString())
                }

                Resource.Loading -> TODO()
                is Resource.Success<*> -> {
                    _isLoading.value = false
                    _isLoggedOutSuccessful.emit(true)
                }

            }
        }

    }


}