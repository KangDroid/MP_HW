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

    // Search Mode
    private var isSearchModeLocal: Boolean = true

    // Livedata communicating with searchFragment
    var searchResult: MutableLiveData<List<Word>> = MutableLiveData()

    var searchUrl: MutableLiveData<String> = MutableLiveData()

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
        if (isSearchModeLocal) {
            viewModelScope.launch {
                searchResult.value = wordRepository.searchWordList(searchQuery)
            }
        } else {
            searchUrl.value = "https://en.dict.naver.com/#/search?range=all&query=${searchQuery}"
        }
    }

    fun setSearchModeLocal() {
        isSearchModeLocal = true
    }

    fun setSearchModeInternet() {
        isSearchModeLocal = false
    }

}