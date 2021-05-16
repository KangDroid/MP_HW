package com.kangdroid.vocabapplication.data.entity.question

data class OEQuestion(
    var allowedAnswer: Set<String>,
    var chosenAnswerOE: String? = null
)