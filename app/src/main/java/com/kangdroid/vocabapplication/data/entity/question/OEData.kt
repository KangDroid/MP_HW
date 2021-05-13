package com.kangdroid.vocabapplication.data.entity.question

import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordCategory

data class OEData(
    var categoryList: List<WordCategory>,
    var questionNumber: Int,
    var targetWord: Word,
    var allowedAnswer: Set<String>,
    var chosenAnswer: String? = null
)
