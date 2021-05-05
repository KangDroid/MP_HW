package com.kangdroid.vocabapplication.data.entity.word

import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(
    entities = [Word::class],
    version = 1
)
abstract class WordDatabase: RoomDatabase() {
    abstract fun getWordDao(): WordDao
}