package com.kangdroid.vocabapplication.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.QuestionData
import com.kangdroid.vocabapplication.databinding.ListenWriteBinding

class ListenOERecyclerAdapter(
    private val imageClickListener: (QuestionData) -> Unit
): RecyclerView.Adapter<ListenOERecyclerAdapter.ListenOERecyclerViewHolder>() {
    var listenQuestionData: List<QuestionData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ListenOERecyclerViewHolder(private val listenWriteBinding: ListenWriteBinding): RecyclerView.ViewHolder(listenWriteBinding.root) {
        init {
            listenWriteBinding.listenItemView.setOnClickListener {
                imageClickListener(listenQuestionData[adapterPosition])
            }

            listenWriteBinding.inputWord.addTextChangedListener {
                listenQuestionData[adapterPosition].listenOEQuestion!!.inputAnswer = (it ?: "").toString()
            }
        }

        fun bind(questionData: QuestionData) {
            listenWriteBinding.listenTitle.text = listenWriteBinding.inputWord.context.getString(R.string.listen_write_title, questionData.questionNumber)
            listenWriteBinding.inputWord.setText(questionData.listenOEQuestion!!.inputAnswer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListenOERecyclerViewHolder {
        return ListenOERecyclerViewHolder(
            ListenWriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListenOERecyclerViewHolder, position: Int) {
        holder.bind(listenQuestionData[position])
    }

    override fun getItemCount(): Int = listenQuestionData.size
}