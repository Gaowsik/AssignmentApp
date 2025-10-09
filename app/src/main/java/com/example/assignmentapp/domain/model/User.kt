package com.example.assignmentapp.domain.model

import com.example.assignmentapp.data.source.local.entity.UserEntity

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {

    fun toUserEntity(): UserEntity {
        return UserEntity(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )

    }
}