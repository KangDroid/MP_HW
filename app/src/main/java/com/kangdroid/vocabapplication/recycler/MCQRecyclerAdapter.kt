package com.kangdroid.vocabapplication.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.QuestionData
import com.kangdroid.vocabapplication.databinding.McqRowBinding

class MCQRecyclerAdapter: RecyclerView.Adapter<MCQRecyclerAdapter.MCQRecyclerViewHolder>() {
    private var mcqData: List<QuestionData> = listOf()

    inner class MCQRecyclerViewHolder(private val mcqRowBinding: McqRowBinding): RecyclerView.ViewHolder(mcqRowBinding.root) {
        fun bind(questionData: QuestionData) {
            mcqRowBinding.mcqGroup.clearCheck()
            mcqRowBinding.multipleChoiceTitle.text = mcqRowBinding.multipleChoiceTitle.context.getString(R.string.question_title, questionData.questionNumber, questionData.targetWord.word)
            val choiceList: List<RadioButton> = listOf(mcqRowBinding.firstChoice, mcqRowBinding.secondChoice, mcqRowBinding.thirdChoice, mcqRowBinding.fourthChoice)
            for (i in choiceList.indices) {
                choiceList[i].text = questionData.choiceList!![i]
                choiceList[i].isChecked = (questionData.chosenAnswerMCQ == i)
                choiceList[i].setOnClickListener {
                    Log.d(this::class.java.simpleName, "SetOnClick Called!")
                    questionData.chosenAnswerMCQ = i
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MCQRecyclerViewHolder {
        return MCQRecyclerViewHolder(
            McqRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MCQRecyclerViewHolder, position: Int) {
        holder.bind(mcqData[position])
    }

    override fun getItemCount(): Int = mcqData.size

    fun setQuestionData(inputData: List<QuestionData>) {
        mcqData = inputData
        notifyDataSetChanged()
    }

    fun getSolvedQuestionData(): List<QuestionData> = mcqData
}