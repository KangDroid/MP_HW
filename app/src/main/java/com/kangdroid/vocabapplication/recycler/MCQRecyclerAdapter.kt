package com.kangdroid.vocabapplication.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.MCQData
import com.kangdroid.vocabapplication.databinding.McqRowBinding

class MCQRecyclerAdapter: RecyclerView.Adapter<MCQRecyclerAdapter.MCQRecyclerViewHolder>() {
    private var mcqData: List<MCQData> = listOf()

    inner class MCQRecyclerViewHolder(private val mcqRowBinding: McqRowBinding): RecyclerView.ViewHolder(mcqRowBinding.root) {
        fun bind(questionData: MCQData) {
            mcqRowBinding.mcqGroup.clearCheck()
            mcqRowBinding.multipleChoiceTitle.text = mcqRowBinding.multipleChoiceTitle.context.getString(R.string.question_title, questionData.questionNumber, questionData.targetWord)
            val choiceList: List<RadioButton> = listOf(mcqRowBinding.firstChoice, mcqRowBinding.secondChoice, mcqRowBinding.thirdChoice, mcqRowBinding.fourthChoice)
            for (i in choiceList.indices) {
                choiceList[i].text = questionData.choiceList[i]
                choiceList[i].isChecked = (questionData.chosenAnswer == i)
                choiceList[i].setOnClickListener {
                    Log.d(this::class.java.simpleName, "SetOnClick Called!")
                    questionData.chosenAnswer = i
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

    fun setQuestionData(inputData: List<MCQData>) {
        mcqData = inputData
        notifyDataSetChanged()
    }

    fun getSolvedQuestionData(): List<MCQData> = mcqData
}