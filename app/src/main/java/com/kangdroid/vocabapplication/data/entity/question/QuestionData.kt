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
    var mcqQuestionData: MCQQuestion? = null,

    // OE
    var oeQuestionData: OEQuestion? = null,

    // Listen Choice
    var listenChoice: ListenChoice? = null,

    // Listen OE
    var listenOEQuestion: ListenOE? = null
)