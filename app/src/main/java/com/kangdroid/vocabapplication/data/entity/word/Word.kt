package com.kangdroid.vocabapplication.data.entity.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
class Word(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "word") var word: String,
    @ColumnInfo(name = "meaning") var meaning: String
) {
}