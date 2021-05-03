package com.kangdroid.vocabapplication.data.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.entity.user.UserDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat

class UserRepositoryTest {
    private lateinit var userDatabase: UserDatabase
    private lateinit var userRepository: UserRepository

    @Before
    fun init() {
        userDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            UserDatabase::class.java
        ).build()
        userRepository = UserRepository(userDao = userDatabase.getUserDao())
    }

    @After
    fun destroy() {
        userDatabase.close()
    }

    @Test
    fun is_getAllUsers_returns_empty_list() {
        val list: List<User> = userRepository.getAllUsers()
        assertThat(list.size).isEqualTo(0)
    }
}