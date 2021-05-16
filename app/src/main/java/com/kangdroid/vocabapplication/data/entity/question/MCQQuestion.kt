package com.kangdroid.vocabapplication.data.entity.question

data class MCQQuestion(
    var actualAnswer: Int,
    var choiceList: List<String>,
    var chosenAnswerMCQ: Int? = null
)