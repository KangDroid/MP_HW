package com.kangdroid.vocabapplication.recycler

import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.R
import com.kangdroid.vocabapplication.data.entity.question.QuestionData
import com.kangdroid.vocabapplication.databinding.ListenRowBinding

class ListenChoiceRecyclerAdapter(
    private val imageClickListener: (QuestionData) -> Unit
): RecyclerView.Adapter<ListenChoiceRecyclerAdapter.ListenChoiceRecyclerViewHolder>() {
    var listenChoiceData: List<QuestionData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class ListenChoiceRecyclerViewHolder(private val listenRowBinding: ListenRowBinding): RecyclerView.ViewHolder(listenRowBinding.root) {
        private val choiceList: List<RadioButton> = listOf(listenRowBinding.listenFirstChoice, listenRowBinding.listenSecondChoice, listenRowBinding.listenThirdChoice, listenRowBinding.listenFourthChoice)

        init {
            listenRowBinding.listenItemView.setOnClickListener {
                Log.d(this::class.java.simpleName, "Clicked Image!")
                imageClickListener(listenChoiceData[adapterPosition])
            }
            for (i in choiceList.indices) {
                choiceList[i].setOnClickListener {
                    listenChoiceData[adapterPosition].listenChoice!!.chosenAnswer = i
                }
            }
        }

        fun bind(questionData: QuestionData) {
            listenRowBinding.listenMcq.clearCheck()
            listenRowBinding.listenTitle.text = listenRowBinding.listenTitle.context.getString(R.string.listen_title, questionData.questionNumber)
            for (i in choiceList.indices) {
                choiceList[i].text = questionData.listenChoice!!.choiceList[i]
                choiceList[i].isChecked = (questionData.listenChoice!!.chosenAnswer == i)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListenChoiceRecyclerViewHolder {
        return ListenChoiceRecyclerViewHolder(
            ListenRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListenChoiceRecyclerViewHolder, position: Int) {
        holder.bind(listenChoiceData[position])
    }

    override fun getItemCount(): Int = listenChoiceData.size
}