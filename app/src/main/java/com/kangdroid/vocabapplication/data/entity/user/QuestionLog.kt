package com.kangdroid.vocabapplication.data.entity.user

import com.kangdroid.vocabapplication.data.entity.question.MCQData

class QuestionLog(
    var time: Long = System.currentTimeMillis(),
    var totalCount: Int,
    var correctCount: Int,
)