package com.kangdroid.vocabapplication.data.entity.question

enum class QuestionIdentifier {
    QUESTION_DEFAULT, QUESTION_MCQ, QUESTION_OE, QUESTION_LISTENING, QUESTION_LISTEN_WRITE
}

data class LearnList(
    var questionTitle: String,
    var questionDetails: String,
    var questionIdentifier: QuestionIdentifier = QuestionIdentifier.QUESTION_DEFAULT
)
