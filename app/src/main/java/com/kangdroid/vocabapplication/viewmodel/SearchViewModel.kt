package com.kangdroid.vocabapplication.viewmodel

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kangdroid.vocabapplication.data.entity.word.Word
import com.kangdroid.vocabapplication.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wordRepository: WordRepository
): ViewModel() {

    // Livedata communicating with searchFragment
    var searchResult: MutableLiveData<List<Word>> = MutableLiveData()

    // Search TextViewListener
    val searchViewTextListener: SearchView.OnQueryTextListener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            Log.d("SearchView", "Submitted: $query")
            query?.let {
                searchWord(it)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }
    }

    fun searchWord(searchQuery: String) {
        viewModelScope.launch {
            searchResult.value = wordRepository.searchWordList(searchQuery)
        }
    }

    fun initiateAllWord() {
        viewModelScope.launch {
            searchResult.value = wordRepository.getAllWord()
        }
    }

}