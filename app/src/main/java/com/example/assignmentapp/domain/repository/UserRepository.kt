package com.example.assignmentapp.domain.repository

import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.User

interface UserRepository {

    suspend fun insertUser(user: User): Resource<Unit>

    suspend fun login(email: String, password: String): Resource<User?>

    suspend fun logout(): Resource<Unit>

    suspend fun isLoggedIn(): Boolean

    suspend fun getCurrentUser(): Resource<User?>
}