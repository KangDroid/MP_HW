package com.kangdroid.vocabapplication.data.entity.question

import com.kangdroid.vocabapplication.data.entity.word.Word

data class MCQData(
    var questionNumber: Int,
    var targetWord: Word,
    var actualAnswer: Int,
    var choiceList: List<String>,
    var chosenAnswer: Int? = null
)