package com.example.assignmentapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assignmentapp.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {
    fun toUser(): User {
        return User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )
    }
}