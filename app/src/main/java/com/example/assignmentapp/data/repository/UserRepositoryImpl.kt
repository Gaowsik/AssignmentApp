package com.example.assignmentapp.data.repository

import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.data.source.local.PrefDataStore
import com.example.assignmentapp.data.source.local.UserDataSource
import com.example.assignmentapp.domain.model.User
import com.example.assignmentapp.domain.repository.UserRepository
import com.example.assignmentapp.utils.AppConstants
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val prefDataStore: PrefDataStore
) : UserRepository {
    override suspend fun insertUser(user: User) = userDataSource.insertUser(user)


    override suspend fun login(email: String, password: String): Resource<User?> {
        val result = userDataSource.login(email, password)
        return when (result) {
            is Resource.Success -> {
                if (result.value == null) {
                    Resource.Error(Exception("Invalid email or password"))
                } else {
                    updateIsLoggedInToPref(true)
                    updateLoggedUserEmail(email)
                    result

                }
            }

            is Resource.Error -> result
            Resource.Loading -> TODO()
        }
    }

    override suspend fun logout() = prefDataStore.clearDataStore()
    override suspend fun isLoggedIn() =
        prefDataStore.getBooleanFromPref(AppConstants.PREF_IS_SINGED_IN, false)

    override suspend fun getCurrentUser(): Resource<User?> {
        val email = getCurrentUserEmail()

        return when (val result = userDataSource.getUserByEmail(email)) {
            is Resource.Error -> Resource.Error(result.exception)
            Resource.Loading -> Resource.Loading
            is Resource.Success<*> -> {
                val user = result.value as? User
                if (user == null) {
                    Resource.Error(Exception("Invalid email or password"))
                } else {
                    Resource.Success(user)
                }
            }
        }
    }


    suspend fun updateIsLoggedInToPref(status: Boolean) {
        prefDataStore.addBooleanToPref(AppConstants.PREF_IS_SINGED_IN, status)
    }

    suspend fun updateLoggedUserEmail(email: String) {
        prefDataStore.addStringToPref(AppConstants.PREF_LOGGED_USER_EMAIL, email)
    }

    suspend fun getCurrentUserEmail() =
        prefDataStore.getStringFromPref(AppConstants.PREF_LOGGED_USER_EMAIL, "")


}