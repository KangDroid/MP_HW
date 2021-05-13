package com.kangdroid.vocabapplication.data.entity.question

import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordCategory

data class MCQData(
    var categoryList: List<WordCategory>,
    var questionNumber: Int,
    var targetWord: Word,
    var actualAnswer: Int,
    var choiceList: List<String>,
    var chosenAnswer: Int? = null
)