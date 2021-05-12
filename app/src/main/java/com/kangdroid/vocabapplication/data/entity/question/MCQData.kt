package com.kangdroid.vocabapplication.data.entity.question

data class MCQData(
    var questionNumber: Int,
    var targetWord: String,
    var actualAnswer: Int,
    var choiceList: List<String>,
    var chosenAnswer: Int? = null
)