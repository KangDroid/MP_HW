package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.user.UserSession
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordCategory
import com.kangdroid.vocabapplication.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wordRepository: WordRepository
): ViewModel() {
    // Log Tag
    private val logTag: String = this::class.java.simpleName

    // Live data communicating with LoginFragment
    var randomWordList: MutableLiveData<MutableList<Word>> = MutableLiveData()

    private val wordShowTotal: Int = 60
    private val eachWeakPreserve: Int = 15

    fun setRandomWordList() {
        Log.d(logTag, "Shuffling word data..")
        viewModelScope.launch {
            // Prioritized word category[Weak category comes first]
            val removedCategory: List<WordCategory> = getRemovedCategory()
            val weakCategoryList: MutableList<WordCategory> = mutableListOf()

            // Add Weak category if exists
            UserSession.currentUser?.let {
                weakCategoryList.addAll(it.weakCategory)
            }

            // Total Word List[Random]
            val totalWordList: MutableList<Word> = wordRepository.getAllWord().toMutableList().apply {
                shuffle()
            }
            val wordList: MutableList<Word> = mutableListOf()

            weakCategoryList.forEach {
                val categoryWordSize: List<Word> = totalWordList.filter { eachWord ->
                    eachWord.category == it.name.toLowerCase()
                }

                if (categoryWordSize.size <= eachWeakPreserve) {
                    totalWordList.removeAll(categoryWordSize)
                    wordList.addAll(categoryWordSize)
                } else {
                    val selectedList: List<Word> = categoryWordSize.shuffled().take(eachWeakPreserve)
                    totalWordList.removeAll(selectedList)
                    wordList.addAll(selectedList)
                }
            }

            wordList.addAll(totalWordList.shuffled().take(wordShowTotal - wordList.size))

            Log.d(logTag, "Added WordList: ${wordList.size}")

            randomWordList.value = wordList
        }
    }

    private fun getRemovedCategory(): List<WordCategory> = enumValues<WordCategory>().toMutableSet().apply {
        UserSession.currentUser?.let {
            it.weakCategory.forEach { wordCategory ->
                // Remove on wordCategoryList
                remove(wordCategory)
            }
        }
    }.toList()
}