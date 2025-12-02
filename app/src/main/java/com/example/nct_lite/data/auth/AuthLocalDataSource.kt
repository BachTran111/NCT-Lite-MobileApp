package com.example.nct_lite.data.auth

import com.example.nct_lite.data.auth.model.User
import com.example.nct_lite.data.auth.model.UserDao

class AuthLocalDataSource (private val userDao: UserDao) {
    suspend fun saveUser(user: User) = userDao.saveUser(user)
    suspend fun getUser() = userDao.getUser()
    suspend fun clear() = userDao.clear()
}