package com.kangdroid.vocabapplication.data.repository

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.entity.user.UserDao
import com.kangdroid.vocabapplication.data.entity.user.UserDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private val logTag: String = this::class.java.simpleName

    // Init for checking whether singleton is REAL singleton
    init {
        Log.d(logTag, "Initiating UserRepository!")
    }

    suspend fun getAllUsers(): List<UserDto> {
        Log.d(logTag, "Accessing whole user information..")
        return userDao.getAllUser().map {
            UserDto(
                id = it.id,
                userName = it.userName,
                userPassword = it.userPassword,
                weakCategory = objectMapper.readValue(it.weakCategory)
            )
        }.toList()
    }

    suspend fun addUser(user: UserDto) {
        Log.d(logTag, "Adding Users: $user")
        return userDao.addUser(user.toUser())
    }

    suspend fun findUserByName(userName: String): UserDto {
        Log.d(logTag, "Finding user with username: $userName")
        val userObject: User = userDao.findUserByUserName(userName)
            ?: throw IllegalStateException("Cannot find username with $userName!")
        return UserDto(
            id = userObject.id,
            userName = userObject.userName,
            userPassword = userObject.userPassword,
            weakCategory = objectMapper.readValue(userObject.weakCategory)
        )
    }

    suspend fun deleteUser(userDto: UserDto) {
        Log.d(logTag, "Removing user with username: ${userDto.userName}")
        userDao.deleteUser(userDto.toUser())
    }

    suspend fun updateUser(userDto: UserDto) {
        Log.d(logTag, "Changing user with username: ${userDto.userName}")
        userDao.updateUser(userDto.toUser())
    }
}