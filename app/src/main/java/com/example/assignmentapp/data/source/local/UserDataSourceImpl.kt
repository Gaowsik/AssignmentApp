package com.example.assignmentapp.data.source.local

import com.example.assignmentapp.data.BaseRepo
import com.example.assignmentapp.data.Resource
import com.example.assignmentapp.data.source.local.entity.UserEntity

class UserDataSourceImpl(private val userDao: UserDao) : UserDataSource, BaseRepo() {
    override suspend fun insertUser(user: UserEntity): Resource<Unit> = safeCall{
        userDao.insertUser(user)
    }

    override suspend fun getUserByEmail(email: String): Resource<UserEntity?> = safeCall {
       userDao.getUserByEmail(email)
    }

    override suspend fun login(email: String, password: String): Resource<UserEntity?> = safeCall{
        userDao.login(email,password)
    }
}