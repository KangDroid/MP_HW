package com.kangdroid.vocabapplication.data.entity.word

import androidx.room.Dao
import androidx.room.Query
import javax.inject.Singleton

@Singleton
@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    suspend fun getAllWordList(): List<Word>
}