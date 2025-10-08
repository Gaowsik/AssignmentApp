package com.example.assignmentapp.data.source.local

import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.data.source.local.entity.UserEntity

interface UserDataSource {

    suspend fun insertUser(user: UserEntity): Resource<Unit>

    suspend fun getUserByEmail(email: String): Resource<UserEntity?>

    suspend fun login(email: String, password: String): Resource<UserEntity?>
}