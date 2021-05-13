package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.question.MCQData
import com.kangdroid.vocabapplication.data.entity.question.OEData
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
class OEViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository
): ViewModel() {

    private val totalQuestionCount: Int = 20
    private val weakWordCount: Int = 4

    var oeQuestionList: MutableLiveData<List<OEData>> = MutableLiveData()
    var questionResult: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    fun removeQuestionResult() {
        questionResult.value = null
    }

    fun createQuestion() {
        // User[Session]
        val user: UserDto = UserSession.currentUser ?: run {
            Log.e(this::class.java.simpleName, "Cannot get user session!!")
            return
        }

        viewModelScope.launch {
            val categoryToChoose: Int = 5 - user.weakCategory.size
            val categoryList: MutableList<WordCategory> = user.weakCategory.toMutableList().apply {
                addAll(
                    enumValues<WordCategory>().filter { !user.weakCategory.contains(it) }.shuffled().take(categoryToChoose)
                )
            }
            oeQuestionList.value = categoryToQuestion(categoryList)
        }
    }

    fun markQuestions(questionList: List<OEData>) {
        // User[Session]
        val user: UserDto = UserSession.currentUser ?: run {
            Log.e(this::class.java.simpleName, "Cannot get user session!!")
            return
        }

        val correctCount: Int = questionList.filter {it.allowedAnswer.contains(it.chosenAnswer)}.size
        Log.d(this::class.java.simpleName, "Correct: $correctCount out of $totalQuestionCount")

        val newWeakCategoryPair: MutableList<Pair<WordCategory, Float>> = mutableListOf()
        questionList[0].categoryList.forEach { eachCategory ->
            val categoryWordList: List<OEData> = questionList.filter {it.targetWord.category.toLowerCase() == eachCategory.name.toLowerCase()}
            val correctWordList: List<OEData> = categoryWordList.filter {it.allowedAnswer.contains(it.chosenAnswer)}
            val score: Float = (correctWordList.size.toFloat() / categoryWordList.size) * 100
            newWeakCategoryPair.add(Pair(eachCategory, score))

            Log.d(this::class.java.simpleName, "Category: ${eachCategory.name}, Q Size: ${categoryWordList.size}, Correct: ${correctWordList.size}, Score: $score")
        }

        // Sort them
        val newWeakCategory: List<WordCategory> = newWeakCategoryPair.sortedWith(compareBy {it.second}).map {
            Log.d(this::class.java.simpleName, "New Category: ${it.first.name}")
            it.first
        }.take(3)

        // Convert to Set
        user.weakCategory = newWeakCategory.toSet()

        // Update user
        questionResult.value = Pair<Int, Int>(correctCount, totalQuestionCount)

        // Save userdata back to DB!
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    private suspend fun categoryToQuestion(categoryList: List<WordCategory>): MutableList<OEData> {
        // Question List
        val questionList: MutableList<OEData> = mutableListOf()

        categoryList.forEach {
            val wordList: List<Word> = wordRepository.findByCategory(it).shuffled().take(weakWordCount).onEach { eachWord ->
                Log.d(this::class.java.simpleName, "Word: ${eachWord.word}, Meaning: ${eachWord.meaning}")
                questionList.add(
                    OEData(
                        categoryList = categoryList,
                        questionNumber = questionList.size+1,
                        targetWord = eachWord,
                        allowedAnswer = eachWord.meaning.split(',').toSet()
                    )
                )
            }
        }

        return questionList
    }
}