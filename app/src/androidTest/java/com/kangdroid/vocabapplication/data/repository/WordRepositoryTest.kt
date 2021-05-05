package com.kangdroid.vocabapplication.data.repository

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordDatabase
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test

class WordRepositoryTest {
    private lateinit var wordDatabase: WordDatabase
    private lateinit var wordRepository: WordRepository

    @Before
    fun init() {
        wordDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            WordDatabase::class.java
        ).build()
        wordRepository = WordRepository(wordDao = wordDatabase.getWordDao())
    }

    @After
    fun destroy() {
        wordDatabase.close()
    }

    @Test
    fun is_getAllUsers_returns_empty_list() {
        val list: List<Word> = runBlocking {
            wordRepository.getAllWord()
        }
        Assertions.assertThat(list.size).isEqualTo(0)
    }
}