package com.example.assignmentapp.domain.model

import com.example.assignmentapp.data.source.local.entity.UserEntity

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {

    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )

    }
}