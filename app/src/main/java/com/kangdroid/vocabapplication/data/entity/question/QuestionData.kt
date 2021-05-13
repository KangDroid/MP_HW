package com.kangdroid.vocabapplication.data.entity.question

import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordCategory

data class QuestionData(
    // Essentials
    var questionType: QuestionType,
    var categoryList: List<WordCategory>,
    var questionNumber: Int,
    var targetWord: Word,

    // MCQ
    var actualAnswer: Int? = null,
    var choiceList: List<String>? = null,
    var chosenAnswerMCQ: Int? = null,

    // OE
    var allowedAnswer: Set<String>? = null,
    var chosenAnswerOE: String? = null,

    // Listen Choice
    var listenChoice: ListenChoice? = null
)