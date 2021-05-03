package com.kangdroid.vocabapplication.data.entity.user

import androidx.room.Dao
import androidx.room.Query
import javax.inject.Singleton

@Singleton
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>
}