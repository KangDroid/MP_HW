package com.kangdroid.vocabapplication.data.entity.word

import androidx.room.Dao
import androidx.room.Query
import javax.inject.Singleton

@Singleton
@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    suspend fun getAllWordList(): List<Word>

    @Query("SELECT * FROM word WHERE word LIKE :searchQuery OR meaning LIKE :searchQuery")
    suspend fun searchWordList(searchQuery: String): List<Word>

    @Query("SELECT * FROM word WHERE category is :categoryQuery")
    suspend fun searchWordCategory(categoryQuery: String): List<Word>

    @Query("SELECT * FROM word WHERE meaning LIKE '%%' AND NOT meaning=:filter")
    suspend fun searchWordMeaningAndFilter(filter: String): List<Word>
}