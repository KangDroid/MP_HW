package com.kangdroid.vocabapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class LearnPageRequest {
    REQUEST_LEARN_MAIN, REQUEST_MCQ, REQUEST_OE, REQUEST_LISTEN_MCQ, REQUEST_LISTEN_OE
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

    fun requestOE() {
        pageRequest.value = LearnPageRequest.REQUEST_OE
    }

    fun requestListenMCQ()  {
        pageRequest.value = LearnPageRequest.REQUEST_LISTEN_MCQ
    }

    fun requestListenOE() {
        pageRequest.value = LearnPageRequest.REQUEST_LISTEN_OE
    }
}