package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.question.MCQData
import com.kangdroid.vocabapplication.data.entity.user.QuestionLog
import com.kangdroid.vocabapplication.data.entity.user.UserDto
import com.kangdroid.vocabapplication.data.entity.user.UserSession
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MCQViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository
): ViewModel() {
    var questionData: MutableLiveData<List<MCQData>> = MutableLiveData()
    var questionResult: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    private val totalQuestionCount: Int = 20
    private val weakWordCount: Int = 4

    fun markQuestions(questionList: List<MCQData>) {
        // User[Session]
        val user: UserDto = UserSession.currentUser ?: run {
            Log.e(this::class.java.simpleName, "Cannot get user session!!")
            return
        }

        val correctCount: Int = questionList.filter {
            it.actualAnswer == it.chosenAnswer
        }.size
        Log.d(this::class.java.simpleName, "Correct: $correctCount out of $totalQuestionCount")

        user.questionLog.add(
            QuestionLog(
                questionList = questionList,
                correctCount = correctCount
            )
        )

        viewModelScope.launch {
            userRepository.updateUser(user)
            questionResult.value = Pair<Int, Int>(correctCount, totalQuestionCount)
        }
    }

    fun removeQuestionResult() {
        questionResult.value = null
    }

    fun prepareQuestionData() {
        // User[Session]
        val user: UserDto = UserSession.currentUser ?: run {
            Log.e(this::class.java.simpleName, "Cannot get user session!!")
            return
        }

        viewModelScope.launch {
            // Total Word List
            val totalWordList: MutableList<Word> = wordRepository.getAllWord().toMutableList()
            val questionList: MutableList<MCQData> = weakCategoryToQuestion(user, totalWordList)

            // Add questions
            val targetWordList: List<Word> = totalWordList.shuffled().take(totalQuestionCount - questionList.size).onEach {

                var actualAnswer: Int = -1
                val choice: List<String> = prepareMeaning(it)
                for (i in choice.indices) {
                    if (choice[i] == it.meaning) {
                        actualAnswer = i
                        break
                    }
                }

                questionList.add(
                    MCQData(
                        questionNumber = questionList.size+1,
                        targetWord = it,
                        actualAnswer = actualAnswer,
                        choiceList = choice
                    )
                )
            }

            questionData.value = questionList
        }
    }

    private suspend fun weakCategoryToQuestion(user: UserDto, totalWordList: MutableList<Word>): MutableList<MCQData> {
        // Question List
        val questionList: MutableList<MCQData> = mutableListOf()

        user.weakCategory.forEach {
            val wordList: List<Word> = wordRepository.findByCategory(it).shuffled().take(weakWordCount).onEach { eachWord ->
                totalWordList.removeIf { target ->
                    eachWord.id == target.id
                }

                var actualAnswer: Int = -1
                val choice: List<String> = prepareMeaning(eachWord)
                for (i in choice.indices) {
                    if (choice[i] == eachWord.meaning) {
                        actualAnswer = i
                        break
                    }
                }

                questionList.add(
                    MCQData(
                        questionNumber = questionList.size+1,
                        targetWord = eachWord,
                        actualAnswer = actualAnswer,
                        choiceList = choice
                    )
                )
            }
        }

        return questionList
    }

    private suspend fun prepareMeaning(targetWord: Word): List<String> {
        val targetList: MutableList<String> = mutableListOf()
        targetList.add(targetWord.meaning)

        val wordListFiltered: List<String> = wordRepository.searchWordMeaningAndFilter(targetWord.meaning).shuffled().take(3).map {
            it.meaning
        }

        targetList.addAll(wordListFiltered)

        return targetList.shuffled()
    }
}