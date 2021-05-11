package com.kangdroid.vocabapplication.data.entity.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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
}