package com.kangdroid.vocabapplication.data.entity.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kangdroid.vocabapplication.data.entity.word.WordCategory

class UserDto(
    var id: Long?,
    var userName: String,
    var userPassword: String,
    var weakCategory: Set<WordCategory>, // Should be serialized in json
) {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    override fun toString(): String {
        return "User Name: $userName"
    }

    fun toUser(): User = User(
        id = id,
        userName = userName,
        userPassword = userPassword,
        weakCategory = objectMapper.writeValueAsString(weakCategory)
    )
}