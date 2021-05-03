package com.kangdroid.vocabapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.user.User
import com.kangdroid.vocabapplication.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    var databaseEmptyLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun requestDBCheck() {
        viewModelScope.launch {
            lateinit var userList: List<User>
            with(Dispatchers.IO) {
                userList = userRepository.getAllUsers()
            }

            with(Dispatchers.Main) {
                databaseEmptyLiveData.value = userList.isEmpty()
            }
        }
    }
}