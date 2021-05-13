package com.kangdroid.vocabapplication.data.entity.user

import com.kangdroid.vocabapplication.data.entity.question.QuestionData

class QuestionLog(
    var time: Long = System.currentTimeMillis(),
    var questionList: List<QuestionData>,
    var totalCount: Int,
    var correctCount: Int,
)