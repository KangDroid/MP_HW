package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wordRepository: WordRepository
): ViewModel() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // Live data communicating with LoginFragment
    var randomWordList: MutableLiveData<MutableList<Word>> = MutableLiveData()

    fun setRandomWordList() {
        Log.d(logTag, "Shuffling word data..")
        viewModelScope.launch {
            val wordList: MutableList<Word> = wordRepository.getAllWord().toMutableList().apply {
                shuffle()
            }
            randomWordList.value = wordList
        }
    }
}