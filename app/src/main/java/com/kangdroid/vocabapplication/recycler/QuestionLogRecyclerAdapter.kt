package com.kangdroid.vocabapplication.recycler

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.user.QuestionLog
import com.kangdroid.vocabapplication.databinding.QuestionLogRowBinding
import java.util.*

class QuestionLogRecyclerAdapter(private val questionLogList: List<QuestionLog>): RecyclerView.Adapter<QuestionLogRecyclerAdapter.QuestionLogRecyclerViewHolder>() {
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MM.dd HH:mm")
    inner class QuestionLogRecyclerViewHolder(private val questionLogRowBinding: QuestionLogRowBinding): RecyclerView.ViewHolder(questionLogRowBinding.root) {
        fun bind(questionLog: QuestionLog) {
            questionLogRowBinding.questionDate.text = questionLogRowBinding.questionDate.context.getString(R.string.question_held, simpleDateFormat.format(Date(questionLog.time)))
            questionLogRowBinding.questionScore.text = questionLogRowBinding.questionScore.context.getString(R.string.question_score, questionLog.correctCount, questionLog.totalCount)
            questionLogRowBinding.questionType.text = questionLog.questionList[0].questionType.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionLogRecyclerViewHolder {
        return QuestionLogRecyclerViewHolder(
            QuestionLogRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuestionLogRecyclerViewHolder, position: Int) {
        holder.bind(questionLogList[position])
    }

    override fun getItemCount(): Int = questionLogList.size
}