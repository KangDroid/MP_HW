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

    private val wordShowTotal: Int = 40
    private val weakWordCount: Int = 24 // 60% of total
    private val restWordCount: Int = wordShowTotal - weakWordCount // Rest

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

            // Add Priority Category
            var categoryIterator: Int = 0
            var toRemoveWord: Word
            for (i in 0 until weakWordCount) {
                wordList.add(
                    totalWordList.find {
                        it.category == weakCategoryList[categoryIterator].name.toLowerCase(Locale.ROOT)
                    }!!.apply {toRemoveWord = this}
                )
                // Remove word
                totalWordList.removeIf {
                    it.id == toRemoveWord.id || it.word == toRemoveWord.word
                }

                categoryIterator++
                categoryIterator %= weakCategoryList.size
            }

            // Rest of word List
            wordList.addAll(totalWordList.shuffled().take(restWordCount))
            Log.d(logTag, "Put Priority Word: ${wordList.size}")

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