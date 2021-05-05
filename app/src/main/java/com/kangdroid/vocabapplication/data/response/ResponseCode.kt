package com.kangdroid.vocabapplication.data.response

enum class ResponseCode(private val errorMessage: String) {
    REGISTER_OK("Register Completed!"),
    REGISTER_DUPLICATED_ID("Username already exists!"),
    REGISTER_UNKNOWN_FAILURE("Unknown Error Occurred.")
}