package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.data.response.ResponseCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // Database empty or NOT checker!
    var registerNeeded: MutableLiveData<Boolean> = MutableLiveData()

    // Response of registering users
    var registerResponseLiveData: MutableLiveData<ResponseCode> = MutableLiveData()

    // Whether login succeed or not
    var loginSucceed: MutableLiveData<ResponseCode> = MutableLiveData()

    // Check whether DB is empty or not
    fun requestDBCheck() {
        viewModelScope.launch {
            registerNeeded.value = userRepository.getAllUsers().isEmpty()
        }
    }

    // Register Value
    fun registerUser(userName: String, userPassword: String) {
        viewModelScope.launch {
            runCatching {
                userRepository.findUserByName(userName)
            }.onSuccess {
                Log.w(logTag, "Duplicated name exists!")
                registerResponseLiveData.value = ResponseCode.REGISTER_DUPLICATED_ID
            }.onFailure {
                Log.d(logTag, "No duplicate username found.")

                // Add User
                userRepository.addUser(User(null, userName, userPassword))

                // Register Completed!
                registerResponseLiveData.value = ResponseCode.REGISTER_OK
            }
        }
    }

    // Login User
    fun loginUser(userName: String, userPassword: String) {
        viewModelScope.launch {
            runCatching {
                userRepository.findUserByName(userName)
            }.onSuccess {
                // When ID is correct
                whenIDCorrect(it, userName, userPassword)
            }.onFailure {
                // When ID is incorrect / not-found
                whenIDIncorrect(userName)
            }
        }
    }

    // Procedure/steps to execute when ID is correct.
    private fun whenIDCorrect(user: User, userName: String, userPassword: String) {
        val isCredentialCorrect: Boolean = (user.userPassword == userPassword && user.userName == userName)

        // When credentials are not correct.
        if (!isCredentialCorrect) {
            loginSucceed.value = ResponseCode.LOGIN_PASSWORD_INCORRECT
        } else {
            loginSucceed.value = ResponseCode.LOGIN_OK
        }
    }

    // Procedure/steps to execute when ID is not found.
    private fun whenIDIncorrect(userName: String) {
        Log.w(logTag, "Username requested with: $userName not found.")
        loginSucceed.value = ResponseCode.LOGIN_USERNAME_NOT_FOUND
    }

    fun setRegisterCompleted() {
        registerNeeded.value = false
    }

    fun setRegisterNeeded() {
        registerNeeded.value = true
    }
}