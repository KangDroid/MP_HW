package com.kangdroid.vocabapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class LearnPageRequest {
    REQUEST_LEARN_MAIN, REQUEST_MCQ
}

@HiltViewModel
class LearnViewModel @Inject constructor(): ViewModel() {
    var pageRequest: MutableLiveData<LearnPageRequest> = MutableLiveData()

    fun requestLearnPage() {
        pageRequest.value = LearnPageRequest.REQUEST_LEARN_MAIN
    }

    fun requestMCQ() {
        pageRequest.value = LearnPageRequest.REQUEST_MCQ
    }
}