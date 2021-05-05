package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.data.response.ResponseCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    var databaseEmptyLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var registerResponseLiveData: MutableLiveData<ResponseCode> = MutableLiveData()

    fun requestDBCheck() {
        viewModelScope.launch {
            databaseEmptyLiveData.value =  userRepository.getAllUsers().isEmpty()
        }
    }

    // Register Value
    fun registerUser(userName: String, userPassword: String) {
        viewModelScope.launch {
            runCatching {
                userRepository.findUserByName(userName)
            }.onSuccess {
                Log.w(this::class.java.simpleName, "Duplicated name exists!")
                registerResponseLiveData.value = ResponseCode.REGISTER_DUPLICATED_ID
            }.onFailure {
                Log.d(this::class.java.simpleName, "No duplicate username found.")

                // Add User
                userRepository.addUser(
                    User(
                        id = null,
                        userName = userName,
                        userPassword = userPassword
                    )
                )

                // Register Completed!
                registerResponseLiveData.value = ResponseCode.REGISTER_OK
            }
        }
    }
}