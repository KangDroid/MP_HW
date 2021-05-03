package com.kangdroid.vocabapplication.data.repository

import android.util.Log
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
}