package com.kangdroid.vocabapplication.data.repository

import android.util.Log
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.entity.user.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    private val logTag: String = this::class.java.simpleName

    // Init for checking whether singleton is REAL singleton
    init {
        Log.d(logTag, "Initiating UserRepository!")
    }

    suspend fun getAllUsers(): List<User> {
        Log.d(logTag, "Accessing whole user information..")
        return userDao.getAllUser()
    }

    suspend fun addUser(user: User) {
        Log.d(logTag, "Adding Users: $user")
        return userDao.addUser(user)
    }

    suspend fun findUserByName(userName: String): User {
        Log.d(logTag, "Finding user with username: $userName")
        return userDao.findUserByUserName(userName)
            ?: throw IllegalStateException("Cannot find username with $userName!")
    }
}