package com.kangdroid.vocabapplication.data.entity.question

enum class QuestionIdentifier {
    QUESTION_DEFAULT, QUESTION_MCQ
}

data class LearnList(
    var questionTitle: String,
    var questionDetails: String,
    var questionIdentifier: QuestionIdentifier = QuestionIdentifier.QUESTION_DEFAULT
)
