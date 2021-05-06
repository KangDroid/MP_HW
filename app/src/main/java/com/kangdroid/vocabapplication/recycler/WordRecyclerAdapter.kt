package com.kangdroid.vocabapplication.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.databinding.HomeRowBinding
import javax.inject.Inject
import javax.inject.Singleton

class WordRecyclerAdapter: RecyclerView.Adapter<WordRecyclerAdapter.HomeRecyclerViewHolder>() {

    // Word Data
    private var randomWordData: MutableList<Word> = mutableListOf()

    // Word Meaning expanded or not?
    private var wordMeaningExposed: MutableList<Boolean> = mutableListOf()

    // View Holder
    inner class HomeRecyclerViewHolder(private val homeRowBinding: HomeRowBinding): RecyclerView.ViewHolder(homeRowBinding.root) {
        fun bindView(word: Word) {
            homeRowBinding.wordView.text = word.word
            homeRowBinding.meaningView.text = word.meaning
            homeRowBinding.meaningView.visibility = updateVisibility()

            homeRowBinding.showMeaning.setOnClickListener {
                wordMeaningExposed[adapterPosition] = !wordMeaningExposed[adapterPosition]
                homeRowBinding.meaningView.visibility = updateVisibility()
            }
        }

        private fun updateVisibility(): Int {
            return if (!wordMeaningExposed[adapterPosition]) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return HomeRecyclerViewHolder(
            HomeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        holder.bindView(randomWordData[position])
    }

    override fun getItemCount(): Int = randomWordData.size

    fun setRandomWordData(wordList: MutableList<Word>) {
        randomWordData = wordList
        wordMeaningExposed = MutableList(randomWordData.size) {false}
        notifyDataSetChanged()
    }
}