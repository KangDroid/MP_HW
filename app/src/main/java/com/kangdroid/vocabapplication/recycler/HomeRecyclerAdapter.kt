package com.kangdroid.vocabapplication.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.databinding.HomeRowBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRecyclerAdapter @Inject constructor(): RecyclerView.Adapter<HomeRecyclerAdapter.HomeRecyclerViewHolder>() {

    // Word Data
    private var randomWordData: MutableList<Word> = mutableListOf()

    // View Holder
    inner class HomeRecyclerViewHolder(private val homeRowBinding: HomeRowBinding): RecyclerView.ViewHolder(homeRowBinding.root) {
        fun bindView(word: Word) {
            homeRowBinding.wordView.text = word.word
            homeRowBinding.meaningView.text = word.meaning
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
        notifyDataSetChanged()
    }
}