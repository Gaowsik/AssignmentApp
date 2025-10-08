package com.example.assignmentapp.data.source.local

import com.example.assignmentapp.data.BaseRepo
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.domain.model.User

class UserDataSourceImpl(private val userDao: UserDao) : UserDataSource, BaseRepo() {
    override suspend fun insertUser(user: User) = safeCall {
        userDao.insertUser(user.toUserEntity())
    }

    override suspend fun getUserByEmail(email: String): Resource<User?> = safeCall {
        userDao.getUserByEmail(email)?.toUser()
    }

    override suspend fun login(email: String, password: String): Resource<User?> = safeCall {
        userDao.login(email, password)?.toUser()
    }
}