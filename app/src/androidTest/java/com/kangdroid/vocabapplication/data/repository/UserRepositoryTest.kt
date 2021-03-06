package com.kangdroid.vocabapplication.data.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.entity.user.UserDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

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
        val list: List<User> = runBlocking {
            userRepository.getAllUsers()
        }
        assertThat(list.size).isEqualTo(0)
    }

    @Test
    fun is_addUsers_success() {
        val mockUser: User = User(
            id = null,
            userName = "KangDroid",
            userPassword = "test"
        )

        runBlocking {
            runCatching {
                userRepository.addUser(mockUser)
            }.onFailure {
                println(it.stackTraceToString())
                fail("Somehow it failed with unknown error!")
            }.onSuccess {
                val userList: List<User> = userRepository.getAllUsers()
                assertThat(userList.size).isEqualTo(1)
                assertThat(userList[0].userName).isEqualTo(mockUser.userName)
                assertThat(userList[0].userPassword).isEqualTo(mockUser.userPassword)
            }
        }
    }

    @Test
    fun is_findUserByName_throws_IllegalStateException_no_user() {
        runBlocking {
            runCatching {
                userRepository.findUserByName("non-name")
            }.onSuccess {
                fail("This should be failed because db is empty.")
            }.onFailure {
                assertThat(it is IllegalStateException).isEqualTo(true)
                assertThat(it.message).isEqualTo("Cannot find username with non-name!")
            }
        }
    }

    @Test
    fun is_findUserByName_works_well() {
        val mockUser: User = User(
            id = null,
            userName = "KangDroid",
            userPassword = "test"
        )
        runBlocking {
            userRepository.addUser(mockUser)
            runCatching {
                userRepository.findUserByName(mockUser.userName)
            }.onFailure {
                println(it.stackTraceToString())
                fail("We saved mock value but cannot find?")
            }.onSuccess {
                assertThat(it.userName).isEqualTo(it.userName)
                assertThat(it.userPassword).isEqualTo(it.userPassword)
            }
        }
    }
}