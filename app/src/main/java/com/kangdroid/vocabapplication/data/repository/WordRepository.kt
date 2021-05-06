package com.kangdroid.vocabapplication.data.repository

import android.util.Log
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepository @Inject constructor(
    private val wordDao: WordDao
) {
    private val logTag: String = this::class.java.simpleName

    suspend fun getAllWord(): List<Word> {
        Log.d(logTag, "Getting whole word list..")
        return wordDao.getAllWordList()
    }

    suspend fun searchWordList(searchQuery: String): List<Word> {
        Log.d(logTag, "Searching word with: $searchQuery")
        val newQueryString: String = "%${searchQuery}%"
        return wordDao.searchWordList(newQueryString)
    }
}