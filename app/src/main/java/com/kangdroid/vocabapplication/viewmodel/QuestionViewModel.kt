package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.question.ListenChoice
import com.kangdroid.vocabapplication.data.entity.question.ListenOE
import com.kangdroid.vocabapplication.data.entity.question.QuestionData
import com.kangdroid.vocabapplication.data.entity.question.QuestionType
import com.kangdroid.vocabapplication.data.entity.user.QuestionLog
import com.kangdroid.vocabapplication.data.entity.user.UserDto
import com.kangdroid.vocabapplication.data.entity.user.UserSession
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.entity.word.WordCategory
import com.kangdroid.vocabapplication.data.repository.UserRepository
import com.kangdroid.vocabapplication.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val totalQuestionCount: Int = 20
    private val weakWordCount: Int = 4

    var totalQuestionList: MutableLiveData<List<QuestionData>> = MutableLiveData()
    var questionResult: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    fun removeQuestionResult() {
        questionResult.value = null
        totalQuestionList.value = null
    }

    fun createQuestion(questionType: QuestionType) {
        val user: UserDto = getUserSession()
        viewModelScope.launch {
            val wordCategoryList: List<WordCategory> = getShuffledWordCategory(user)
            val questionList: List<QuestionData> = categoryToQuestion(wordCategoryList, questionType)
            totalQuestionList.value = questionList
        }
    }

    fun markQuestions(questionList: List<QuestionData>) {
        // User DTO
        val user: UserDto = getUserSession()

        // Get Correct Count
        val correctCount: Int = getCorrectCount(questionList).size
        Log.d(this::class.java.simpleName, "Correct: $correctCount out of $totalQuestionCount")

        // Save Question Data
        user.questionLog.add(
            QuestionLog(
                totalCount = totalQuestionCount,
                correctCount = correctCount,
                questionList = questionList
            )
        )

        user.weakCategory = getNewWeakCategory(questionList)
        questionResult.value = Pair(correctCount, totalQuestionCount)
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    private fun getNewWeakCategory(questionList: List<QuestionData>): Set<WordCategory> {
        val newWeakCategoryPair: MutableList<Pair<WordCategory, Float>> = mutableListOf()
        questionList[0].categoryList.forEach { eachCategory ->
            // Get Category List
            val categoryWordList: List<QuestionData> = questionList.filter {it.targetWord.category.toLowerCase() == eachCategory.name.toLowerCase()}
            // Get Correct Word List
            val correctWordList: List<QuestionData> = getCorrectCount(categoryWordList)

            // Calculate Score
            val score: Float = (correctWordList.size.toFloat() / categoryWordList.size) * 100
            newWeakCategoryPair.add(Pair(eachCategory, score))

            Log.d(this::class.java.simpleName, "Category: ${eachCategory.name}, Q Size: ${categoryWordList.size}, Correct: ${correctWordList.size}, Score: $score")
        }

        return newWeakCategoryPair.sortedWith(compareBy {it.second}).map {
            Log.d(this::class.java.simpleName, "New Category: ${it.first.name}")
            it.first
        }.take(3).toSet()
    }

    private fun getCorrectCount(questionList: List<QuestionData>): List<QuestionData> = questionList.filter {
        when (questionList[0].questionType) {
            QuestionType.QUESTION_MCQ -> {it.actualAnswer == it.chosenAnswerMCQ}
            QuestionType.QUESTION_OE -> {it.allowedAnswer!!.contains(it.chosenAnswerOE ?: "")}
            QuestionType.QUESTION_LISTEN_CHOICE -> {it.listenChoice!!.actualAnswer == it.listenChoice!!.chosenAnswer}
            QuestionType.QUESTION_LISTEN_OE -> {it.listenOEQuestion!!.actualAnswer == it.listenOEQuestion!!.inputAnswer}
        }
    }

    private suspend fun categoryToQuestion(wordCategoryList: List<WordCategory>, questionType: QuestionType): List<QuestionData> {
        // Dummy Question List
        val questionList: MutableList<QuestionData> = mutableListOf()

        wordCategoryList.forEach {
            wordRepository.findByCategory(it).shuffled().take(weakWordCount).onEach { eachWord ->
                val meaningList: List<String>? = if (questionType == QuestionType.QUESTION_MCQ || questionType == QuestionType.QUESTION_LISTEN_CHOICE) prepareMeaning(eachWord) else null
                questionList.add(
                    QuestionData(
                        questionType = questionType,
                        categoryList = wordCategoryList,
                        targetWord = eachWord,
                        questionNumber = questionList.size+1,
                        choiceList = if (questionType == QuestionType.QUESTION_MCQ) meaningList!! else null,
                        actualAnswer = if (questionType == QuestionType.QUESTION_MCQ) findAnswerMCQ(eachWord, meaningList!!) else null,
                        allowedAnswer = if (questionType == QuestionType.QUESTION_OE) getMeaningList(eachWord) else null,
                        listenChoice = if (questionType == QuestionType.QUESTION_LISTEN_CHOICE) {
                            ListenChoice(
                                actualAnswer = findAnswerMCQ(eachWord, meaningList!!),
                                choiceList = meaningList
                            )
                        } else {
                            null
                        },
                        listenOEQuestion = if (questionType == QuestionType.QUESTION_LISTEN_OE) {
                            ListenOE(
                                actualAnswer = eachWord.word
                            )
                        } else {
                            null
                        }
                    )
                )
            }
        }

        return questionList
    }

    private fun getUserSession(): UserDto = UserSession.currentUser ?: run {
        Log.e(this::class.java.simpleName, "Cannot get user session!!")
        throw IllegalStateException("Cannot get user session!!")
    }

    private fun getShuffledWordCategory(user: UserDto): List<WordCategory> {
        val categoryToChoose: Int = 5 - user.weakCategory.size

        return user.weakCategory.toMutableList().apply {
            addAll(
                enumValues<WordCategory>().filter { !user.weakCategory.contains(it) }.shuffled().take(categoryToChoose)
            )
        }
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

    private fun findAnswerMCQ(eachWord: Word, choice: List<String>): Int {
        var actualAnswer: Int = -1
        for (i in choice.indices) {
            if (choice[i] == eachWord.meaning) {
                actualAnswer = i
                break
            }
        }

        return actualAnswer
    }

    private fun getMeaningList(eachWord: Word): Set<String> = eachWord.meaning.split(',').toSet()
}