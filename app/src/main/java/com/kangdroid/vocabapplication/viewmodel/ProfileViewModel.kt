package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.user.UserDto
import com.kangdroid.vocabapplication.data.entity.user.UserSession
import com.kangdroid.vocabapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val isRemoveSucceed: MutableLiveData<Boolean> = MutableLiveData()

    fun removeData() {
        // Get Current User
        val user: UserDto = UserSession.currentUser ?: run {
            Log.d(this::class.java.simpleName, "Cannot get user session!")
            isRemoveSucceed.value = false
            return
        }

        runCatching {
            viewModelScope.launch {
                userRepository.deleteUser(user)
            }
        }.onFailure {
            removeFailed(user, it)
        }.onSuccess {
            removeSucceed(user)
        }
    }

    private fun removeFailed(user: UserDto, throwable: Throwable) {
        Log.e(this::class.java.simpleName, "Error occurred when removing userdata.")
        Log.e(this::class.java.simpleName, "Target User Name: ${user.userName}")
        Log.e(this::class.java.simpleName, "StackTrace: ${throwable.stackTraceToString()}")
        isRemoveSucceed.value = false
    }

    private fun removeSucceed(user: UserDto) {
        Log.d(this::class.java.simpleName, "Successfully removed user with username: ${user.userName}")
        isRemoveSucceed.value = true
    }
}