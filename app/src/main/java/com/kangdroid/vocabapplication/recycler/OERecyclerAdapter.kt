package com.kangdroid.vocabapplication.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.OEData
import com.kangdroid.vocabapplication.databinding.OeRowBinding

class OERecyclerAdapter: RecyclerView.Adapter<OERecyclerAdapter.OERecyclerViewHolder>() {
    private var questionList: List<OEData> = listOf()

    inner class OERecyclerViewHolder(private val oeRowBinding: OeRowBinding): RecyclerView.ViewHolder(oeRowBinding.root) {
        init {
            oeRowBinding.inputMeaning.addTextChangedListener {
                questionList[adapterPosition].chosenAnswer = it?.toString()
            }
        }
        fun bind(oeData: OEData) {
            oeRowBinding.oeTitle.text = oeRowBinding.oeTitle.context.getString(R.string.question_title, oeData.questionNumber, oeData.targetWord.word)
            oeRowBinding.inputMeaning.setText(oeData.chosenAnswer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OERecyclerViewHolder {
        return OERecyclerViewHolder(
            OeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OERecyclerViewHolder, position: Int) {
        holder.bind(questionList[position])
    }

    override fun getItemCount(): Int  = questionList.size

    fun setQuestionData(inputData: List<OEData>) {
        questionList = inputData
        notifyDataSetChanged()
    }

    fun getSolvedQuestionData(): List<OEData> = questionList
}