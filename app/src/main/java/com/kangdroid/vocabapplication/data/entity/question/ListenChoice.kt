package com.kangdroid.vocabapplication.data.entity.question

data class ListenChoice(
    var actualAnswer: Int,
    var choiceList: List<String>,
    var chosenAnswer: Int? = null
)
