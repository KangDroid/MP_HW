package com.kangdroid.vocabapplication.data.response

enum class ResponseCode(private val errorMessage: String) {
    REGISTER_OK("Register Completed!"),
    REGISTER_DUPLICATED_ID("Username already exists!"),
    REGISTER_UNKNOWN_FAILURE("Unknown Error Occurred."),
    LOGIN_USERNAME_NOT_FOUND("Username Not Found!"),
    LOGIN_PASSWORD_INCORRECT("Password Incorrect!"),
    LOGIN_OK("Login OK!")
}