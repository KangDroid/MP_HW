package com.kangdroid.vocabapplication.data.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "username") var userName: String,
    @ColumnInfo(name = "password") var userPassword: String
) {
    override fun toString(): String {
        return "User Name: $userName"
    }
}