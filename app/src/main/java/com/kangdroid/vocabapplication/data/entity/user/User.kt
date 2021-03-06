package com.kangdroid.vocabapplication.data.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "username") var userName: String,
    @ColumnInfo(name = "password") var userPassword: String,
    @ColumnInfo(name = "weakcategory") var weakCategory: String, // Should be serialized in json
    @ColumnInfo(name = "questionlog") var questionLog: String // Should be serialized in json
) {
    override fun toString(): String {
        return "User Name: $userName"
    }
}