package com.kangdroid.vocabapplication.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.data.entity.question.LearnList
import com.kangdroid.vocabapplication.data.entity.question.QuestionIdentifier
import com.kangdroid.vocabapplication.databinding.LearnRowBinding

class LearnRecyclerAdapter(private val onClickListener: (QuestionIdentifier) -> Unit): RecyclerView.Adapter<LearnRecyclerAdapter.LearnRecyclerViewHolder>() {
    private val questionTypeList: List<LearnList> = listOf(
        LearnList(
            questionTitle = "Word - MCQ",
            questionDetails = "Multiple Choice Question!",
            questionIdentifier = QuestionIdentifier.QUESTION_MCQ
        )
    )
    inner class LearnRecyclerViewHolder(private val learnRowBinding: LearnRowBinding): RecyclerView.ViewHolder(learnRowBinding.root) {
        fun bind(learnList: LearnList) {
            learnRowBinding.questionTitle.text = learnList.questionTitle
            learnRowBinding.questionDetails.text = learnList.questionDetails
            learnRowBinding.learnCardView.setOnClickListener {
                onClickListener(learnList.questionIdentifier)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnRecyclerViewHolder {
        return LearnRecyclerViewHolder(
            LearnRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LearnRecyclerViewHolder, position: Int) {
        holder.bind(questionTypeList[position])
    }

    override fun getItemCount(): Int = questionTypeList.size
}