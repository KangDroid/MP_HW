package com.kangdroid.vocabapplication.data.response

enum class ResponseCode() {
    REQUIRED_LOGIN,
    REQUIRED_REGISTER,
    REQUIRED_NOTHING,
    REGISTER_OK,
    REGISTER_DUPLICATED_ID,
    REGISTER_UNKNOWN_FAILURE,
    LOGIN_USERNAME_NOT_FOUND,
    LOGIN_PASSWORD_INCORRECT,
    LOGIN_OK
}