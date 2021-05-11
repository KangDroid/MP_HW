package com.kangdroid.vocabapplication.data.entity.user

import androidx.room.*
import javax.inject.Singleton

@Singleton
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAllUser(): List<User>

    @Insert
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user WHERE username IS :userName")
    suspend fun findUserByUserName(userName: String): User?

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}