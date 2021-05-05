package com.kangdroid.vocabapplication.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.entity.user.UserDao
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.data.response.ResponseCode
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class FakeRepository: UserDao {
    override suspend fun getAllUser(): List<User> {
        return listOf()
    }

    override suspend fun addUser(user: User) {
    }

    override suspend fun findUserByUserName(userName: String): User? {
        return null
    }
}

class LoginViewModelTest {
    // Rule that every android-thread should launched in single thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val userRepository: UserRepository by lazy {
        UserRepository(FakeRepository())
    }

    // View Model
    private val loginViewModel: LoginViewModel by lazy {
        LoginViewModel(userRepository)
    }

    /* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

    @Test
    fun is_requestDBCheck_returns_false() {
        runBlocking {
            loginViewModel.requestDBCheck()
        }

        assertThat(loginViewModel.registerNeeded.getOrAwaitValue()).isEqualTo(true)
    }

    @Test
    fun is_registerUser_returns_REGISTER_OK() {
        runBlocking {
            loginViewModel.registerUser("KangDroid", "whatever")
        }

        assertThat(loginViewModel.registerResponseLiveData.getOrAwaitValue()).isEqualTo(ResponseCode.REGISTER_OK)
    }

    @Test
    fun is_loginUser_works_well() {
        class LoginFakeRepository: UserDao {
            override suspend fun getAllUser(): List<User> = listOf()

            override suspend fun addUser(user: User) = Unit

            override suspend fun findUserByUserName(userName: String): User = User(
                null, "kangdroid", "testtest!"
            )

        }
        val loginUserRepository: UserRepository by lazy {
            UserRepository(LoginFakeRepository())
        }

        // View Model
        val loginViewModel: LoginViewModel by lazy {
            LoginViewModel(loginUserRepository)
        }

        runBlocking {
            loginViewModel.loginUser("kangdroid", "testtest!")
        }

        assertThat(loginViewModel.loginSucceed.getOrAwaitValue()).isEqualTo(ResponseCode.LOGIN_OK)
    }

    @Test
    fun is_loginUser_wrong_when_user_not_found() {
        class LoginFakeRepository: UserDao {
            override suspend fun getAllUser(): List<User> = listOf()

            override suspend fun addUser(user: User) = Unit

            override suspend fun findUserByUserName(userName: String): User? = null

        }
        val loginUserRepository: UserRepository by lazy {
            UserRepository(LoginFakeRepository())
        }

        // View Model
        val loginViewModel: LoginViewModel by lazy {
            LoginViewModel(loginUserRepository)
        }

        runBlocking {
            loginViewModel.loginUser("kangdroid", "testtest!")
        }

        assertThat(loginViewModel.loginSucceed.getOrAwaitValue()).isEqualTo(ResponseCode.LOGIN_USERNAME_NOT_FOUND)
    }

    @Test
    fun is_loginUser_wrong_pw_not_correct() {
        class LoginFakeRepository: UserDao {
            override suspend fun getAllUser(): List<User> = listOf()

            override suspend fun addUser(user: User) = Unit

            override suspend fun findUserByUserName(userName: String): User = User(
                null, "kangdroid", "asdf!"
            )

        }
        val loginUserRepository: UserRepository by lazy {
            UserRepository(LoginFakeRepository())
        }

        // View Model
        val loginViewModel: LoginViewModel by lazy {
            LoginViewModel(loginUserRepository)
        }

        runBlocking {
            loginViewModel.loginUser("kangdroid", "testtest!")
        }

        assertThat(loginViewModel.loginSucceed.getOrAwaitValue()).isEqualTo(ResponseCode.LOGIN_PASSWORD_INCORRECT)
    }
}